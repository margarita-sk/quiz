package com.margarita.quiz.telegram.config;

import com.margarita.quiz.telegram.BotUpdatesListener;
import com.pengrad.telegrambot.ExceptionHandler;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.impl.UpdatesHandler;
import com.pengrad.telegrambot.request.GetUpdates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Configuration
public class Config {

    @Bean
    public TelegramBot telegramBot(@Value("${telegram.token}") String telegramToken) {
        log.info("creating telegramBot");
        return new TelegramBot(telegramToken);
    }

    @Bean
    public UpdatesHandler updatesHandler(TelegramBot telegramBot, ExceptionHandler exceptionHandler, BotUpdatesListener updatesListener) {
        log.info("creating updatesHandler");
        final var updatesHandler = new UpdatesHandler(10);
        updatesHandler.start(telegramBot, updatesListener, exceptionHandler, new GetUpdates());
        return updatesHandler;
    }

    @Bean
    public ExceptionHandler exceptionHandler() {
        return e -> log.error(e.response().description());
    }

    @Bean(name = "latestQuestionByChatId")
    public ConcurrentMap<Long, Long> latestQuestionByChatId() {
        return new ConcurrentHashMap<>();
    }

}
