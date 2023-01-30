package com.example.test.test1.Repository;

import com.example.test.test1.Entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository  extends JpaRepository<Board, Integer> {
    Board findBySubject(String subject);
    Board findBySubjectAndContent(String subject, String content);
    List<Board> findBySubjectLike(String subject);
    Page<Board> findAll(Pageable pageable);
    Page<Board> findByBoardType(Integer type, Pageable pageable);
}
