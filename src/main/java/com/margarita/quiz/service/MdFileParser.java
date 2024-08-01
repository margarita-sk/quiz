package com.margarita.quiz.service;

import com.margarita.quiz.model.QuizQuestion;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
                String htmlContent = mdIntoHtml(content);
                var quizQuestion = new QuizQuestion();
                quizQuestion.setQuestion(summary);
                quizQuestion.setAnswer(htmlContent);
                quizQuestion.setNextDateTime(LocalDateTime.now());
                return quizQuestion;
            } else return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public static String mdIntoHtml(String mdText) {
        Parser parser = Parser.builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder().build();

        // Parse the Markdown text into a Document
        Node document = parser.parse(mdText);

        // Render the Document to HTML
        return renderer.render(document);
    }

}
