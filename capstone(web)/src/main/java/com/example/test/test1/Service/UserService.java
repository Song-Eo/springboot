package com.example.test.test1.Service;

import com.example.test.test1.Entity.Board;
import com.example.test.test1.Entity.SiteUser;
import com.example.test.test1.Exception.DataNotFoundException;
import com.example.test.test1.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SiteUser create(String username, String password){
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setUserrole("ROLE_USER");
        this.userRepository.save(user);
        return user;
    }

    public SiteUser update(SiteUser siteUser, String password, String password2){

        if (password==password2)
            siteUser.setPassword(passwordEncoder.encode(password));
        else{
            throw new DataNotFoundException("두 패스워드가 다릅니다.");
        }
        this.userRepository.save(siteUser);
        return siteUser;
    }

    public SiteUser getUser(String username){
        Optional<SiteUser> siteUser = this.userRepository.findByusername(username);
        if(siteUser.isPresent()){
            return siteUser.get();
        } else{
            throw new DataNotFoundException("siteuser not found");
        }
    }

    public SiteUser getUser(Long id){
        Optional<SiteUser> siteUser = this.userRepository.findById(id);
        if(siteUser.isPresent()){
            return siteUser.get();
        } else{
            throw new DataNotFoundException("siteuser not found");
        }
    }

    public void modify(SiteUser siteUser, String password){
        siteUser.setPassword(password);
        this.userRepository.save(siteUser);
    }
}
