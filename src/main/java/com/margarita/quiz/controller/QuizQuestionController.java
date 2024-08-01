package com.margarita.quiz.controller;

import com.margarita.quiz.model.QuizQuestion;
import com.margarita.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RequiredArgsConstructor
@RequestMapping("/quiz")
@RestController
public class QuizQuestionController {

    private final QuizService quizService;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Hello world");
    }

    @GetMapping("/questions/random")
    public ResponseEntity<QuizQuestion> receiveRandomQuestion() {
        final var question = quizService.findRandomQuestion();
        return ResponseEntity.ok(question.get());
    }

    @PostMapping(value = "/questions", consumes = {MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> saveQuestions(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Please select a file to upload", HttpStatus.BAD_REQUEST);
        }
        quizService.storeQuestionsFromFile(file);
        return ResponseEntity.ok().build();
    }
}
