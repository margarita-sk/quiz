package com.margarita.quiz.telegram.updateprocessor;

import com.margarita.quiz.repository.QuizQuestionRepository;
import com.margarita.quiz.service.MdFileParser;
import com.margarita.quiz.service.QuizService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class UploadMDFileUpdateProcessor implements UpdateProcessor {

    private final TelegramBot telegramBot;
    private final QuizQuestionRepository repository;

    @Override
    public void processUpdate(Update update) {
        var chatId = update.message().chat().id();
        var fileId = update.message().document().fileId();

        var request = new GetFile(fileId);
        var getFileResponse = telegramBot.execute(request);

        File file = getFileResponse.file();
        try {
            var fileContent = new String(telegramBot.getFileContent(file));
            var questions = MdFileParser.parseQuestions(fileContent);
            questions.forEach(question -> question.setNextDateTime(LocalDateTime.of(2000, 2, 2, 2, 2)));
            var savedQuestions = repository.saveAllAndFlush(questions);

            var message = String.format("Quiz questions saved: %s", savedQuestions.size());
            var sendMessage = new SendMessage(chatId, message);
            SendResponse sendResponse = telegramBot.execute(sendMessage);
            if (!sendResponse.isOk()) {
                log.error("Error code: {}, message: {}, chat id: {}", sendResponse.errorCode(), sendResponse.message(), chatId);
            }
        } catch (IOException | DataIntegrityViolationException exception) {
            if (exception.getClass().equals(DataIntegrityViolationException.class)) return;

            throw new RuntimeException(exception.getMessage());
        }

    }
}
