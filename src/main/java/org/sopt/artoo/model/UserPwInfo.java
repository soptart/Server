package org.sopt.artoo.model;

import lombok.Data;

@Data
public class UserPwInfo {
    private String u_pw_current;

    private String u_pw_new;

    private String u_pw_check;
}
