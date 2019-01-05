package org.sopt.artoo.utils;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PurchaseStateMessage {

    private int StatusNum;
    private String PurchaseMessage;

    PurchaseStateMessage(int StatusNum){
        switch(StatusNum){
            case 10 :
                this.PurchaseMessage = BEFORE_SELL;
            case 11:
                this.PurchaseMessage = APPLY_DIRECT_TRANS;
            case 12:
                this.PurchaseMessage = FINISH_DIRECT_PAYMENT;
            case 13:
                this.PurchaseMessage = DECIDE_PURCHASE;
            case 14:
                this.PurchaseMessage = CANCEL_DIRECT_PUCHASE;
            case 15:
                this.PurchaseMessage = SUCCESS_REFUND;
            case 21:
                this.PurchaseMessage = APPLY_POST_TRANS;
            case 22 :
                this.PurchaseMessage = FINISH_POST_PAYMENT;
            case 23:
                this.PurchaseMessage = CURRENT_STATE_POST;
            case 24:
                this.PurchaseMessage = FINISH_POST_STATE;
            case 25:
                this.PurchaseMessage = CANCEL_POST_PURCHASE;
        }
    }

    public static String BEFORE_SELL = "판매 완료 신청 전";

    /* 직거래 State */
    public static String APPLY_DIRECT_TRANS = "직거래 신청";
    public static String FINISH_DIRECT_PAYMENT = "직거래 결제 완료";
    public static String DECIDE_PURCHASE = "구매 확정";
    public static String CANCEL_DIRECT_PUCHASE = "직거래 취소 신청";

    /* 배송 State */
    public static String APPLY_POST_TRANS = "배송 신청";
    public static String FINISH_POST_PAYMENT = "배송 결제 완료";
    public static String CURRENT_STATE_POST = "작품 배송중";
    public static String FINISH_POST_STATE = "배송 완료";
    public static String CANCEL_POST_PURCHASE = "배송 거래 취소 신청";

    public static String SUCCESS_REFUND = "환불 완료";

}
