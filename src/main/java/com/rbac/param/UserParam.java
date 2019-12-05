package com.rbac.param;

public class UserParam {
    private Integer userId;
    private String email;
    private String password;

    public void setUserId(Integer userId) { this.userId = userId; }

    public Integer getUserId() { return userId; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }
}
