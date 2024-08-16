package org.eng_diary.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class WordBasicInfo {

    private String title;
    private String phonetic;
    private List<Phonetic> sounds = new ArrayList<>();
    private List<Meaning> meanings = new ArrayList<>();

    @Getter
    public static class Phonetic {
        private final String country;
        private final String soundUrl;

        public Phonetic(String country, String soundUrl) {
            this.country = country;
            this.soundUrl = soundUrl;
        }
    }

    @Getter
    public static class Meaning {
        private final String partOfSpeech;
        private List<Definitions> definitions = new ArrayList<>();
        private final String[] synonyms;
        private final String[] antonyms;

        public Meaning(String partOfSpeech, List<Definitions> definitions, String[] synonyms, String[] antonyms) {
            this.partOfSpeech = partOfSpeech;
            this.definitions = definitions;
            this.synonyms = synonyms;
            this.antonyms = antonyms;
        }
    }

    @Getter
    public static class Definitions {
        private final String definition;
        private final String example;

        public Definitions(String definition, String example) {
            this.definition = definition;
            this.example = example;
        }
    }

    public void addPhonetic(String country, String soundUrl) {
        this.sounds.add(new Phonetic(country, soundUrl));
    }

}
