package com.rbac.param;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class ValidateTestVO {
    @NotBlank
    private String msg;
    @NotNull
    private Integer id;
//    @NotEmpty
    private List<String> nameList;

}
