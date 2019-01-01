package org.sopt.artoo.model;


import lombok.*;

@Data
public class PurchaseReq {
    private boolean p_isPost;
    //주문 방법
    private String p_recipient;
    //수령인 - 택배시에만
    private String p_detailAddress;
    //수령 주소 - 택배시에만
    private String p_phone;
    //수령인 전화 번호 - 택배시에만
    private int p_payment;
    //상태

    public boolean checkPurchaseReq() {
        if(p_isPost != false){
            return ( p_recipient != null && p_detailAddress != null && p_phone != null && p_payment != 0);
        }
        else {
            return (p_payment != 0);
        }
    }
}
