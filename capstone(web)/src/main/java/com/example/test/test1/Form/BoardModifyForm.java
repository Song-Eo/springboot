package com.example.test.test1.Form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class BoardModifyForm {
    @NotEmpty(message = "내용은 필수항목입니다.")
    private String content;

}
