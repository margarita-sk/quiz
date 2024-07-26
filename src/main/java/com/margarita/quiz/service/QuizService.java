package com.margarita.quiz.service;

import com.margarita.quiz.model.QuizQuestion;
import com.margarita.quiz.repository.QuizQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
public class QuizService {

    public final QuizQuestionRepository repository;

    public Optional<QuizQuestion> findRandomQuestion() {
        var quizQuestions = repository.findByNextDateTimeBefore(now());
        var size = quizQuestions.size();
        if (size == 0) return Optional.empty();

        var randomQuestion = Optional.ofNullable(quizQuestions.get(new Random().nextInt(size)));

        randomQuestion.ifPresent(quizQuestion -> {
            var times = quizQuestion.getTimes();
            LocalDateTime repeatInterval = switch ((int) times) {
                case 0 -> now().plusMinutes(20);
                case 1 -> now().plusHours(8);
                case 2 -> now().plusHours(24);
                default -> now().plusDays(3);
            };

            repository.updateDateTime(quizQuestion.getId(), repeatInterval);
        });
        return randomQuestion;
    }

    public void updateFailedQuestion(Long questionId) {
        repository.updateFailedQuestion(questionId);
    }

    public void storeQuestionsFromFile(MultipartFile file) {
        try {
            String fileContent = new String(file.getBytes(), StandardCharsets.UTF_8);
            List<QuizQuestion> quizQuestions = MdFileParser.parseQuestions(fileContent);
            repository.saveAll(quizQuestions);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

}
