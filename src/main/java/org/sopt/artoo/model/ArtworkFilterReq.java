package org.sopt.artoo.model;

import lombok.Data;

import java.util.List;

@Data
public class ArtworkFilterReq {
    //filter category, size, type
    //작품 사이즈
    String a_size;
    //작품 형태
    String a_form;
    //작품 카테고리
    String a_category;

}
