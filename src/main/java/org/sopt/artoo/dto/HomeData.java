package org.sopt.artoo.dto;

import lombok.Data;
import org.sopt.artoo.dto.ArtworkPic;

import java.util.List;

@Data
public class HomeData {
    int a_idx;
    String a_name;
    String a_year;
    List<ArtworkPic> pic_url;


    /*public HomeData(String a_name, String a_year, List<ArtworkPic> pic_url) {
        this.a_name = a_name;
        this.a_year = a_year;
        this.pic_url = pic_url;
    }*/
}
