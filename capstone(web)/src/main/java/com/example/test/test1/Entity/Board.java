package com.example.test.test1.Entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer boardType; //0 = 커뮤니티, 1 = 랩실, 2 = 학생회, 3 = 프로젝트, 4 = 갤러리, 5 =

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String field;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime recruitDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime modifyDate;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;

    @ManyToOne
    private SiteUser author;

    @ManyToMany
    Set<SiteUser> voter;
}
