package com.margarita.quiz.telegram.updateprocessor;

import com.pengrad.telegrambot.model.Update;

public interface UpdateProcessor {

    void processUpdate(Update update);
}
