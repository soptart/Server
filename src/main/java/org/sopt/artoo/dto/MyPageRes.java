package org.sopt.artoo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.sopt.artoo.utils.ResponseMessage;
import org.sopt.artoo.utils.StatusCode;

@Data
@Builder
@AllArgsConstructor
public class MyPageRes<T> {
    private int status;

    private String message;

    private T data;

    private int dataNum;

    private String userDescription;


    public MyPageRes(final int status, final String message){
        this.status = status;
        this.message = message;
        this.data = null;
        this.dataNum = 0;
        this.userDescription = null;
    }

    public static <T> MyPageRes<T> res(final int status, final String message) {
        return res(status, message, null, 0, null);
    }

    public static <T> MyPageRes<T> res(final int status, final String message, final T t, final int dataNum, final String Description) {
        return MyPageRes.<T>builder()
                .status(status)
                .message(message)
                .data(t)
                .userDescription(Description)
                .dataNum(dataNum)
                .build();
    }

    public static final MyPageRes FAIL_DEFAULT_RES = new MyPageRes(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR);

    public static final MyPageRes FAIL_AUTHORIZATION_RES = new MyPageRes(StatusCode.UNAUTHORIZED, ResponseMessage.UNAUTHORIZED);

}
