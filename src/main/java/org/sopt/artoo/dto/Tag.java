package org.sopt.artoo.dto;

import lombok.Data;

import java.util.List;

@Data
public class Tag {
    //Tag 고유번호
    int t_idx;
    //Tag (ex. 화려한, 조용한)
    String t_shortTag;
    //artwork 고유키(외래키)
    int a_idx;
    //태크 메인제목
    String t_mainTag;
    //태그 서브제목, 눌렀을 때만(t_idx로 받아옴)
    String t_subTag;
    //태그 백그라운드 image
    String t_imgUrl;
    //0번째 List
    List<ArtworkPic> firstTag;


}
