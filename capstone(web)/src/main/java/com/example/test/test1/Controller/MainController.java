package com.example.test.test1.Controller;


import com.example.test.test1.Entity.Board;
import com.example.test.test1.Entity.SiteUser;
import com.example.test.test1.Repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.expression.Lists;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class MainController {

    private final BoardRepository boardRepository;


    @GetMapping("/")
    public String index(){
        return "redirect:/user/login";
    }

    @GetMapping("/main")
    public String main(Model model)
    {

        List<Board> popular = this.boardRepository.findAll();
        Collections.sort(popular, new Comparator<Board>() {
            @Override
            public int compare(Board o1, Board o2) {
                return o2.getVoter().size() - o1.getVoter().size();
            }
        });
        List<Board> recents = this.boardRepository.findAll(Sort.by(Sort.Direction.DESC, "createDate"));
        model.addAttribute("popular", popular);
        model.addAttribute("recents", recents);
        return "main";
    }
}
