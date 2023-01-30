package com.example.test.test1.Controller;

import com.example.test.test1.Entity.SiteUser;
import com.example.test.test1.Form.UserForm;
import com.example.test.test1.Form.UserModifyForm;
import com.example.test.test1.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String login(Model model, Principal principal){
        if (principal != null) {
            model.addAttribute("name", principal.getName());
        }
        return "login";
    }

    @GetMapping("/modify")
    public String modify(UserModifyForm userModifyForm, Principal principal){
        if(principal == null){
            return "redirect:/login";
        }
        return "user_modify";
    }

    @PostMapping("/modify")
    public String userModify(@Valid UserModifyForm userModifyForm, BindingResult bindingResult, Principal principal) {

        if(bindingResult.hasErrors()){
            return "main";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        if(!siteUser.getUsername().equals(principal.getName())) {
            return "redirect:/main";
        }

        this.userService.update(siteUser, userModifyForm.getPassword1(), userModifyForm.getPassword2());
        return String.format("redirect:/main");
    }

    @GetMapping("/signup")
    public String signup(UserForm userForm){
        return "signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserForm userForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "redirect:/user/signup";
        }

        try {
            userService.create(userForm.getUsername(), userForm.getPassword1());
        }catch(DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "redirect:/user/signup";
        }catch(Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "redirect:/user/signup";
        }

        return "redirect:/user/signup";
    }
}
