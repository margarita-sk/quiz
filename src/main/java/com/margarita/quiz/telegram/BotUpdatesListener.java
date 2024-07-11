package com.margarita.quiz.telegram;

import com.margarita.quiz.telegram.updateprocessor.enums.MIME_TYPE;
import com.margarita.quiz.telegram.updateprocessor.enums.UPDATE_TYPE;
import com.margarita.quiz.telegram.updateprocessor.UpdateProcessor;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class BotUpdatesListener implements UpdatesListener {

    private final List<UpdateProcessor> updateProcessors;

    @Override
    public int process(List<Update> updates) {
        updates.stream().forEach(update -> findProcessor(update).processUpdate(update));
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private UpdateProcessor findProcessor(Update update) {
        Class updateProcessorClass = findProcessorClass(update);
        return updateProcessors.stream()
                .filter(updateProcessor -> updateProcessor.getClass().equals(updateProcessorClass))
                .findFirst().orElseThrow();
    }

    private Class findProcessorClass(Update update) {
        Class updateProcessorClass = UPDATE_TYPE.findByText(update.message().text()).getProcessorClass();
        if (update.message().document() != null &&
                MIME_TYPE.MARKDOWN.getType().equals(update.message().document().mimeType())) {
            updateProcessorClass = UPDATE_TYPE.UPLOAD_FILE.getProcessorClass();
        }
        return updateProcessorClass;
    }
}
