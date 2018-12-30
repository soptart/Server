package org.sopt.artoo.dto;

import lombok.Data;

import java.util.List;

@Data
public class HomeData {
    int a_idx;
    String a_name;
    String a_year;
    List<ArtworkPic> pic_url;


}
