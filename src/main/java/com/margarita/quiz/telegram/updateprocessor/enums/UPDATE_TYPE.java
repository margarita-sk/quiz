package com.margarita.quiz.telegram.updateprocessor.enums;

import com.margarita.quiz.telegram.updateprocessor.DefaultUpdateProcessor;
import com.margarita.quiz.telegram.updateprocessor.FailedAnswerUpdateProcessor;
import com.margarita.quiz.telegram.updateprocessor.RandomQuestionUpdateProcessor;
import com.margarita.quiz.telegram.updateprocessor.UploadMDFileUpdateProcessor;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum UPDATE_TYPE {
    GET_RANDOM_QUESTION("/random", RandomQuestionUpdateProcessor.class),
    FAILED_TO_ANSWER("/failed_to_answer", FailedAnswerUpdateProcessor.class),
    UPLOAD_FILE("", UploadMDFileUpdateProcessor.class),
    DEFAULT("/type", DefaultUpdateProcessor.class);


    private final String text;
    private final Class processorClass;

    UPDATE_TYPE(String text, Class processorClass) {
        this.text = text;
        this.processorClass = processorClass;
    }

    public static UPDATE_TYPE findByText(String text) {
        return Arrays.stream(values()).filter(type -> type.getText().equals(text)).findAny().orElse(DEFAULT);
    }
}
