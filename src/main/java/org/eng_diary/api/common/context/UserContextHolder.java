package org.eng_diary.api.common.context;

public class UserContextHolder {
    private static final ThreadLocal<String> userContext = new ThreadLocal<>();

    public static void setUserId(String userId) {
        userContext.set(userId);
    }

    public static String getUserId() {
        return userContext.get();
    }

    public static void clear() {
        userContext.remove();
    }
}

