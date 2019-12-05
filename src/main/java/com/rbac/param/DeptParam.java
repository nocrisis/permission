package com.rbac.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class DeptParam {
    private Integer id;
    @NotBlank(message = "部门名称不可以为空")
    @Length(max = 15, min = 2, message = "部门名称在2-15个字之间")
    private String name;
    private Integer parentId = 0;//防止空指针异常，不传指定为0
    private Integer seq;
    @Length(max = 150, message = "备注的长度需要在150个字之间")
    private String memo;

}
