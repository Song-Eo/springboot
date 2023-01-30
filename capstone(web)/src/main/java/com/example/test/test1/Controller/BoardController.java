package com.example.test.test1.Controller;

import com.example.test.test1.Entity.Answer;
import com.example.test.test1.Entity.Board;
import com.example.test.test1.Entity.SiteUser;
import com.example.test.test1.Form.AnswerForm;
import com.example.test.test1.Form.BoardForm;
import com.example.test.test1.Form.BoardModifyForm;
import com.example.test.test1.Form.ProjectBoardForm;
import com.example.test.test1.Service.AnswerService;
import com.example.test.test1.Service.BoardService;
import com.example.test.test1.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RequestMapping("/board")
@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardService boardService;
    private final UserService userService;
    private final AnswerService answerService;


    @GetMapping("/list/{type}")
    public String list(Model model,@PathVariable("type")Integer type ,@RequestParam(value = "page", defaultValue = "0") int page){

        Page<Board> paging = this.boardService.getList(type, page);
        model.addAttribute("paging", paging);
        if(type == 0 || type == 1 || type == 2){
            return "community_board";
        }
        else{
            return "project_board";
        }

    }


    @GetMapping("/create/{type}")
    public String boardCreate(Model model,@PathVariable("type") Integer type,BoardForm boardForm, Principal principal){
        model.addAttribute("type", type);
        model.addAttribute("name", principal.getName());

        return "board_create";

    }
    @GetMapping("/create/project")
    public String boardCreate(Model model, ProjectBoardForm projectBoardForm, Principal principal){
        model.addAttribute("name", principal.getName());

        return "project_board_create";
    }

    @PostMapping("/create/{type}")
    public String boardCreate( @Valid BoardForm boardForm, @PathVariable("type") Integer type, BindingResult bindingResult, Principal principal){
        if(bindingResult.hasErrors()){
            return "board_create";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());

        this.boardService.create(boardForm.getSubject(), boardForm.getContent(), type, siteUser);

        return "redirect:/board/list/"+type;
    }

    @PostMapping("/create/3/project")
    public String boardCreate( @Valid ProjectBoardForm projectBoardForm, BindingResult bindingResult, Principal principal){
        if(bindingResult.hasErrors()){
            return "project_board_create";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.boardService.projectCreate(projectBoardForm.getSubject(), projectBoardForm.getContent(), 3,
                projectBoardForm.getField(), siteUser);

        return "redirect:/board/list/3";
    }

    @GetMapping("/modify/{id}")
    public String boardmodify(BoardModifyForm boardModifyForm, Model model, @PathVariable("id") Integer id){
        Board board = this.boardService.getBoard(id);
        model.addAttribute("board", board);
        return "modify_board";
    }

    @PostMapping("/modify/{id}")
    public String boardModify(@Valid BoardModifyForm boardModifyForm, BindingResult bindingResult ,@PathVariable("id") Integer id, Principal principal) {

        if(bindingResult.hasErrors()){
            return "modify_board";
        }
        Board board = this.boardService.getBoard(id);
        if(!board.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.boardService.modify(board, boardModifyForm.getContent());
        return String.format("redirect:/board/detail/%s", id);
    }

    @GetMapping("/delete/{id}")
    public String boardDelete(Principal principal, @PathVariable("id") Integer id) {
        Board board = this.boardService.getBoard(id);
        if (!board.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.boardService.delete(board);
        return "redirect:/";
    }

    @GetMapping("/vote/{id}")
    public String boardVote(Principal principal, @PathVariable("id") Integer id) {
        Board board = this.boardService.getBoard(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.boardService.vote(board, siteUser);
        return String.format("redirect:/board/detail/%s", id);
    }



    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm,
    @RequestParam(value = "page", defaultValue = "0") int page){
        Board board = this.boardService.getBoard(id);
        Page<Answer> paging = this.answerService.getList(board, page);
        model.addAttribute("paging", paging);
        model.addAttribute("board", board);
        return "detail_board";

        /*if(board.getBoardType() != 3) {
            return "detail_board";
        }
        else{
            return "detail_project_board";
        }*/
    }
}
