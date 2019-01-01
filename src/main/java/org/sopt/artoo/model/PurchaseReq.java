package org.sopt.artoo.model;


import lombok.*;

@Data
public class PurchaseReq {
    private int p_state;
    //주문 상태 (주문 방법)
    private int a_idx;
    //작품 고유 번호
    private int u_idx;
    //구매자 고유 번호


    public PurchaseReq() {
        this.p_state = -1;
        this.a_idx = -1;
        this.u_idx = -1;
    }

    public boolean checkPurchaseReq() {
        return (
                p_state != -1 && a_idx != -1 && u_idx != -1
        );
    }
}
