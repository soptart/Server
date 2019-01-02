package org.sopt.artoo.model;

import lombok.Data;
import org.sopt.artoo.dto.ArtworkPic;

import java.util.List;

@Data
public class TransactionReq {
    private String a_name;
    private String person_name;
    private int price;
    private boolean isBuyer;
    private ArtworkPic picUrl;
    private String date;
}
