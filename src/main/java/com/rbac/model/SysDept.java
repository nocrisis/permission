package com.rbac.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SysDept {
    private Integer id;

    private String name;

    private Integer parentId;

    private String level;

    private Integer seq;

    private String memo;

    private String operator;

    private Date operatorTime;

    private String operatorIp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level == null ? null : level.trim();
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public Date getOperatorTime() {
        return operatorTime;
    }

    public void setOperatorTime(Date operatorTime) {
        this.operatorTime = operatorTime;
    }

    public String getOperatorIp() {
        return operatorIp;
    }

    public void setOperatorIp(String operatorIp) {
        this.operatorIp = operatorIp == null ? null : operatorIp.trim();
    }
    /*Lombok就是一个实现了"JSR 269 API"的程序（只要程序实现了该API，就能在javac运行的时候得到调用）。在使用javac的过程中，它产生作用的具体流程如下：
    1.Javac对源代码进行分析，生成一棵抽象语法树(AST)
    2.Javac编译过程中调用实现了JSR 269的Lombok程序
    3.此时Lombok就对第一步骤得到的AST进行处理，找到Lombok注解所在类对应的语法树(AST)，然后修改该语法树(AST)，增加Lombok注解定义的相应树节点
    4.Javac使用修改后的抽象语法树(AST)生成字节码文件*/

    /*
    public static class PeopleBuilder {
        private String name;
        private String sex;
        private int age;

        PeopleBuilder() {
        }

        public People.PeopleBuilder name(String name) {
            this.name = name;
            return this;
        }

        public People.PeopleBuilder sex(String sex) {
            this.sex = sex;
            return this;
        }

        public People.PeopleBuilder age(int age) {
            this.age = age;
            return this;
        }

        public People build() {
            return new People(this.name, this.sex, this.age);
        }

        public String toString() {
            return "People.PeopleBuilder(name=" + this.name + ", sex=" + this.sex + ", age=" + this.age + ")";
        }

    }*/
}