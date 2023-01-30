package com.example.test.test1.Service;

import com.example.test.test1.Entity.Board;
import com.example.test.test1.Entity.SiteUser;
import com.example.test.test1.Exception.DataNotFoundException;
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
public class BoardService {

    private final BoardRepository boardRepository;

    public List<Board> getList(){
        return this.boardRepository.findAll();
    }

    public Board getBoard(Integer id){
        Optional<Board> board = this.boardRepository.findById(id);
        if(board.isPresent()){
            return board.get();
        }
        else{
            throw new DataNotFoundException("Board not found");
        }
    }

    public void create(String subject, String content, Integer type, SiteUser siteUser){
        Board board = new Board();
        board.setSubject(subject);
        board.setContent(content);
        board.setCreateDate(LocalDateTime.now());
        board.setBoardType(type);
        board.setAuthor(siteUser);
        this.boardRepository.save(board);
    }

    public void projectCreate(String subject, String content, Integer type,String field, SiteUser siteUser){
        Board board = new Board();
        board.setSubject(subject);
        board.setContent(content);
        board.setCreateDate(LocalDateTime.now());
        board.setBoardType(type);
        board.setField(field);
        board.setAuthor(siteUser);
        this.boardRepository.save(board);
    }

    public void modify(Board board, String content){
        board.setContent(content);
        board.setModifyDate(LocalDateTime.now());
        this.boardRepository.save(board);
    }

    public Page<Board> getList(Integer type, int page){
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        int size;

        if(type == 3) {
            size = 8;
        }
        else{
            size = 18;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(sorts));

        return this.boardRepository.findByBoardType(type, pageable);
    }



    public void delete(Board board){
        this.boardRepository.delete(board);
    }

    public void vote(Board board, SiteUser siteUser){
        if(board.getVoter().equals(siteUser))
            board.getVoter().remove(siteUser);
        else
            board.getVoter().add(siteUser);

        this.boardRepository.save(board);
    }


}
