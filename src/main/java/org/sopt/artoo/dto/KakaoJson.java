package org.sopt.artoo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class KakaoJson {
    private String grant_type;
    private String client_id;
    private String redirect_uri;
    private String code;
}
