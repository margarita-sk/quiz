package com.margarita.quiz.telegram.updateprocessor;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultUpdateProcessor implements UpdateProcessor {

    private final TelegramBot telegramBot;

    @Override
    public void processUpdate(Update update) {
        Long chatId = update.message().chat().id();
        telegramBot.execute(new SendMessage(chatId, "such command was not implemented yet"));
    }
}
