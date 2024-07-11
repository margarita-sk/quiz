package com.margarita.quiz.telegram.updateprocessor.enums;

import lombok.Getter;

@Getter
public enum MIME_TYPE {
    MARKDOWN("text/markdown");

    private final String type;

    MIME_TYPE(String type) {
        this.type = type;
    }
}
