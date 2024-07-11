package com.margarita.quiz.service;

import com.margarita.quiz.model.QuizQuestion;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MdFileParser {
    public static List<QuizQuestion> parseQuestions(String mdContent) {
        var detailsPattern = Pattern.compile("<details>(.*?)</details>", Pattern.DOTALL);
        var summaryPattern = Pattern.compile("<summary>(.*?)</summary>", Pattern.DOTALL);

        var detailsMatcher = detailsPattern.matcher(mdContent);

        List<String> detailsBlocks = new ArrayList<>();
        while (detailsMatcher.find()) {
            detailsBlocks.add(detailsMatcher.group(1));
        }

        return detailsBlocks.stream().map(detailBlock -> {
            Matcher summaryMatcher = summaryPattern.matcher(detailBlock);
            if (summaryMatcher.find()) {
                String summary = summaryMatcher.group(1).trim();
                String content = detailBlock.substring(summaryMatcher.end()).trim();
                var quizQuestion = new QuizQuestion();
                quizQuestion.setQuestion(summary);
                quizQuestion.setAnswer(content);
                return quizQuestion;
            } else return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

}
