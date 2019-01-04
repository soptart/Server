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

    private String u_name;

    private String u_description;

    private T data;

    private int dataNum;

    public MyPageRes(final int status, final String message){
        this.status = status;
        this.message = message;
        this.u_name = u_name;
        this.u_description = null;
        this.data = null;
        this.dataNum = 0;

    }

    public static <T> MyPageRes<T> res(final int status, final String message) {
        return res(status, message, null, null, null, 0);
    }

    public static <T> MyPageRes<T> res(final int status, final String message, final String u_name, final String Description, final T t, final int dataNum) {
        return MyPageRes.<T>builder()
                .status(status)
                .message(message)
                .u_name(u_name)
                .u_description(Description)
                .data(t)
                .dataNum(dataNum)
                .build();
    }

    public static final MyPageRes FAIL_DEFAULT_RES = new MyPageRes(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR);

    public static final MyPageRes FAIL_AUTHORIZATION_RES = new MyPageRes(StatusCode.UNAUTHORIZED, ResponseMessage.UNAUTHORIZED);

}
