package com.margarita.quiz.telegram;

import lombok.Data;

@Data
public class TelegramMessageDto {

    private String question;
    private String answer;

    @Override
    public String toString() {
        return String.format("%s %n%n <span class=\"tg-spoiler\">%s</span>", question, answer);
    }
}
