package org.sopt.artoo.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class ArtworkFilterReq {
    //filter category, size, type
    //작품 사이즈
    private String a_size;
    //작품 형태
    private String a_form;
    //작품 카테고리
    private String a_category;

    private String a_keyword;

    public ArtworkFilterReq(String a_size, String a_form, String a_category, String a_keyword) {
        this.a_size = a_size;
        this.a_form = a_form;
        this.a_category = a_category;
        this.a_keyword = a_keyword;
    }
}
