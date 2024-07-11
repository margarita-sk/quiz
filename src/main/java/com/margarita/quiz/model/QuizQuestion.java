package com.margarita.quiz.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"question"}))
@NoArgsConstructor
public class QuizQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;

    @Lob
    private String answer;

    private String keyword;

    private String topic;

    @Nullable
    private LocalDateTime lastDateTime;

    @Nullable
    private LocalDateTime nextDateTime;

    private long times;
}
