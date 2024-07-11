package com.margarita.quiz.repository;

import com.margarita.quiz.model.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {

    List<QuizQuestion> findByTopic(String topic);

    List<QuizQuestion> findByKeyword(String keyword);

    List<QuizQuestion> findByNextDateTimeBefore(LocalDateTime dateTime);

    List<QuizQuestion> findByNextDateTimeAfterAndTopic(LocalDateTime nextDateTime, String topic);

    List<QuizQuestion> findByNextDateTimeAfterAndKeyword(LocalDateTime nextDateTime, String keyword);

    @Transactional
    @Modifying
    @Query(value = "update quiz_question set last_date_time = now(), next_date_time = :next_date_time, times = times + 1 " +
            "where id = :id", nativeQuery = true)
    int updateDateTime(@Param("id") Long id, @Param("next_date_time") LocalDateTime nextDateTime);

    @Transactional
    @Modifying
    @Query(value = "update quiz_question set last_date_time = now(), next_date_time = now(), times = times + 1 " +
            "where id = :id", nativeQuery = true)
    int updateFailedQuestion(@Param("id") Long id);

//    @Query("")
//    List<QuizQuestion> upsert(List<QuizQuestion> questions);
}
