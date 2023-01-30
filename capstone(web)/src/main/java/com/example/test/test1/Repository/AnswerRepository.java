package com.example.test.test1.Repository;

import com.example.test.test1.Entity.Answer;
import com.example.test.test1.Entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    Page<Answer> findAllByBoard(Board board, Pageable pageable);
}
