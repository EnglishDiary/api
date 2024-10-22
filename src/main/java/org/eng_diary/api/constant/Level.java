package org.eng_diary.api.constant;

import lombok.Getter;

@Getter
public enum Level {
    HIGH("H"),
    MID("M"),
    LOW("L");

    private final String code;

    Level(String code) {
        this.code = code;
    }

    public static Level fromCode(String code) {
        for (Level level : values()) {
            if (level.code.equals(code)) {
                return level;
            }
        }
        throw new IllegalArgumentException("Invalid level code: " + code);
    }

}
