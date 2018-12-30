package org.sopt.artoo.dto;

import lombok.Data;

@Data
public class ArtworkLike {
            private int a_idx;
            //해당 좋아요에 대한 작품 인덱스 (FK)
            private int u_idx;
            //해당 좋아요에 대한 유저 인덱스 (FK)
            private String al_DATETIME;
            //해당 좋아요가 찍힌 시간대
        }
