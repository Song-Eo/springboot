package com.example.test.test1.Service;

import com.example.test.test1.Entity.Answer;
import com.example.test.test1.Entity.Board;
import com.example.test.test1.Entity.SiteUser;
import com.example.test.test1.Exception.DataNotFoundException;
import com.example.test.test1.Repository.AnswerRepository;
import com.example.test.test1.Repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AnswerService {

    private final AnswerRepository answerRepository;

    public Answer getAnswer(Integer id) {
        Optional<Answer> answer = this.answerRepository.findById(id);
        if (answer.isPresent()) {
            return answer.get();
        } else {
            throw new DataNotFoundException("answer not found");
        }
    }

    public void delete(Answer answer) {
        this.answerRepository.delete(answer);
    }
    public Page<Answer> getList(Board board, int page){
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));

        Pageable pageable = PageRequest.of(page, 2, Sort.by(sorts));
        return this.answerRepository.findAllByBoard(board, pageable);
    }

    public void vote(Answer answer, SiteUser siteUser) {
        answer.getVoter().add(siteUser);
        this.answerRepository.save(answer);
    }
    public void create(Board board, String content, SiteUser author){
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        answer.setBoard(board);
        answer.setAuthor(author);
        this.answerRepository.save(answer);
    }
}
