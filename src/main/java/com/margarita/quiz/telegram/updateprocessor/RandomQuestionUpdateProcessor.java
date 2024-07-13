package com.margarita.quiz.telegram.updateprocessor;

import com.margarita.quiz.service.QuizService;
import com.margarita.quiz.model.QuizQuestion;
import com.margarita.quiz.telegram.TelegramMessageDto;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class RandomQuestionUpdateProcessor implements UpdateProcessor {

    private final QuizService quizExecutionService;
    private final TelegramBot telegramBot;
    private final ConcurrentMap<Long, Long> latestQuestionByChatId;

    @Override
    public void processUpdate(Update update) {
        Long chatId = update.message().chat().id();
        Optional<QuizQuestion> quizQuestion = quizExecutionService.findRandomQuestion();
        if (quizQuestion.isEmpty()) {
            telegramBot.execute(new SendMessage(chatId, "no questions"));
            return;
        }

        TelegramMessageDto message = new TelegramMessageDto();
        message.setQuestion(quizQuestion.get().getQuestion());
        message.setAnswer(quizQuestion.get().getAnswer());

        var sendMessage = new SendMessage(chatId, message.toString()).parseMode(ParseMode.HTML);
        var question = new SendMessage(chatId, "Did your answer was correct? \n /yes \n /failed_to_answer").parseMode(ParseMode.HTML);


        SendResponse sendResponse = telegramBot.execute(sendMessage);
        telegramBot.execute(question);
        latestQuestionByChatId.put(chatId, quizQuestion.get().getId());
        if (!sendResponse.isOk()) {
            log.error("Error code: {}, message: {}, chat id: {}", sendResponse.errorCode(), sendResponse.message(), chatId);
        }
    }
}
