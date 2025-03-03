package org.eng_diary.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Paths;

public class DependencyExtractor {
    private static final Pattern VERSION_PATTERN = Pattern.compile("(\\d+(?:\\.\\d+)+(?:-[a-zA-Z0-9]+)*)");

    public static void main(String[] args) {
//        String springBootJarPath = args[0];
        String springBootJarPath = "C:\\dev\\workspace\\diary\\api\\build\\libs\\api-0.0.1-SNAPSHOT.jar";
        try {
            String dependencies = extractDependencies(springBootJarPath);
            System.out.println(dependencies);

            // 결과를 파일로 저장할 수도 있습니다
            Files.write(Paths.get("dependencies.json"), dependencies.getBytes());
        } catch (IOException e) {
            System.err.println("Error extracting dependencies: " + e.getMessage());
        }
    }

    public static String extractDependencies(String springBootJarPath) throws IOException {
        File springBootJar = new File(springBootJarPath);
        if (!springBootJar.exists() || !springBootJar.isFile()) {
            throw new IOException("Invalid Spring Boot JAR file: " + springBootJarPath);
        }

        List<JSONObject> dependencyList = new ArrayList<>();

        try (JarFile jarFile = new JarFile(springBootJar)) {
            jarFile.stream()
                    .filter(entry -> entry.getName().startsWith("BOOT-INF/lib/") && entry.getName().endsWith(".jar"))
                    .forEach(entry -> {
                        try {
                            processLibraryJar(jarFile, entry, dependencyList);
                        } catch (IOException e) {
                            System.err.println("Error processing entry " + entry.getName() + ": " + e.getMessage());
                        }
                    });
        }

        JSONArray jsonArray = new JSONArray(dependencyList);
        return jsonArray.toString(2);
    }

    private static void processLibraryJar(JarFile parentJar, JarEntry libraryEntry, List<JSONObject> dependencyList) throws IOException {
        String libraryJarName = new File(libraryEntry.getName()).getName();

        // 임시 파일로 내부 jar 추출
        Path tempJarPath = Files.createTempFile("temp-lib-", ".jar");
        try (InputStream is = parentJar.getInputStream(libraryEntry)) {
            Files.copy(is, tempJarPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }

        JSONObject dependencyInfo = new JSONObject();

        // jar 파일명에서 라이브러리명과 버전 추출 (모든 jar에 적용)
        extractInfoFromJarName(libraryJarName, dependencyInfo);

        try (JarFile libJarFile = new JarFile(tempJarPath.toFile())) {
            // MANIFEST.MF 확인
            Manifest manifest = libJarFile.getManifest();
            if (manifest != null) {
                extractInfoFromManifest(manifest, dependencyInfo);
            }

            // POM 속성 확인
            JarEntry pomPropertiesEntry = findPomPropertiesEntry(libJarFile);
            if (pomPropertiesEntry != null) {
                extractInfoFromPomProperties(libJarFile, pomPropertiesEntry, dependencyInfo);
            }
        } catch (Exception e) {
            System.err.println("Error reading jar " + libraryJarName + ": " + e.getMessage());
        } finally {
            // 임시 파일 삭제
            try {
                Files.delete(tempJarPath);
            } catch (IOException e) {
                System.err.println("Failed to delete temporary file: " + tempJarPath);
            }
        }

        // title과 version 필드가 없으면 titleFromJar, versionFromJar 값을 사용
        if (!dependencyInfo.has("title")) {
            dependencyInfo.put("title", dependencyInfo.getString("titleFromJar"));
        }

        if (!dependencyInfo.has("version")) {
            dependencyInfo.put("version", dependencyInfo.getString("versionFromJar"));
        }

        dependencyList.add(dependencyInfo);
    }

    private static void extractInfoFromManifest(Manifest manifest, JSONObject dependencyInfo) {
        String implTitle = manifest.getMainAttributes().getValue("Implementation-Title");
        if (implTitle != null && !implTitle.trim().isEmpty()) {
            dependencyInfo.put("title", implTitle.trim());
        }

        // 버전 정보 추출 시도 (Implementation-Version)
        String implVersion = manifest.getMainAttributes().getValue("Implementation-Version");
        if (implVersion != null && !implVersion.trim().isEmpty()) {
            dependencyInfo.put("version", implVersion.trim());
        } else {
            // Bundle-Version 시도
            String bundleVersion = manifest.getMainAttributes().getValue("Bundle-Version");
            if (bundleVersion != null && !bundleVersion.trim().isEmpty()) {
                dependencyInfo.put("version", bundleVersion.trim());
            }
        }
    }

    private static JarEntry findPomPropertiesEntry(JarFile jarFile) {
        // maven 디렉토리 내의 pom.properties 파일 찾기
        return jarFile.stream()
                .filter(entry -> entry.getName().matches("META-INF/maven/.*?/pom.properties"))
                .findFirst()
                .orElse(null);
    }

    private static void extractInfoFromPomProperties(JarFile jarFile, JarEntry pomEntry, JSONObject dependencyInfo) throws IOException {
        Properties pomProperties = new Properties();
        try (InputStream is = jarFile.getInputStream(pomEntry)) {
            pomProperties.load(is);
        }

        // 라이브러리 이름 (artifactId)
        String artifactId = pomProperties.getProperty("artifactId");
        if (!dependencyInfo.has("title") && artifactId != null && !artifactId.trim().isEmpty()) {
            dependencyInfo.put("title", artifactId.trim());
        }

        // 버전 정보
        String version = pomProperties.getProperty("version");
        if (!dependencyInfo.has("version") && version != null && !version.trim().isEmpty()) {
            dependencyInfo.put("version", version.trim());
        }
    }

    private static void extractInfoFromJarName(String jarFileName, JSONObject dependencyInfo) {
        // .jar 확장자 제거
        String nameWithoutExtension = jarFileName.substring(0, jarFileName.lastIndexOf(".jar"));

        // 버전 패턴 찾기
        Matcher versionMatcher = VERSION_PATTERN.matcher(nameWithoutExtension);

        if (versionMatcher.find()) {
            String version = versionMatcher.group(1);
            String title = nameWithoutExtension.substring(0, nameWithoutExtension.indexOf(version)).replaceAll("-$", "");

            dependencyInfo.put("titleFromJar", title);
            dependencyInfo.put("versionFromJar", version);
        } else {
            // 버전을 찾지 못한 경우 파일명 전체를 title로 설정
            dependencyInfo.put("titleFromJar", nameWithoutExtension);
            dependencyInfo.put("versionFromJar", "unknown");
        }
    }
}