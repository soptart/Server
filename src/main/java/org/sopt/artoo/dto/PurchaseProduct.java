package org.sopt.artoo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PurchaseProduct {
    private String artworkName;
    //작품 이름
    private String artistSchool;
    //작가 학교
    private String artistName;
    //작가 이름
    private int artworkPrice;
    //작품 가격
    private int deliveryCharge;
    //작품 배송비
}
