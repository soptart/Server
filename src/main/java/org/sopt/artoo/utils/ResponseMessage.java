package org.sopt.artoo.utils;


public class ResponseMessage {
    /**
     * Home
     */
    public static final String NOT_FOUND_ARTIST = "작가를 찾을 수 없습니다.";
    public static final String READ_ARTIST = "작가 정보 조회 성공";
    public static final String READ_PICTURES = "작품 정보 조회 성공";

    /**
     * Artwork
     */
    public static final String NOT_FOUND_PICTURES = "사진이 등록되지 않은 작품이 있습니다.";
    public static final String NOT_FOUND_ARTWORK = "작품이 없습니다. artwork_idx를 확인하세요.";


    public static final String LOGIN_SUCCESS = "로그인 성공";
    public static final String LOGIN_FAIL = "로그인 실패";

    /* User - 상윤 */

    public static final String READ_USER = "회원 정보 조회 성공";
    public static final String NOT_FOUND_USER = "회원을 찾을 수 없습니다.";
    public static final String ALREADY_USER = "이미 존재하는 Email입니다.";
    public static final String CREATED_USER = "회원 가입 성공";
    public static final String FAIL_CREATE_USER = "회원 가입 실패";
    public static final String UPDATE_USER = "회원 정보 수정 성공";
    public static final String FAIL_UPDATE_USER = "회원 정보 수정 실패";
    public static final String DELETE_USER = "회원 탈퇴 성공";
    public static final String READ_USER_ARTWORK = "회원 작품 조회 성공";
    public static final String READ_USER_LIKES = "회원 좋아요 선택 조회";
    public static final String READ_ALL_TRANSACTION = "회원 거래 내역 조회 성공";
    public static final String READ_FINISHED_TRANSACTION = "회원 거래 후기 조회 성공";
    public static final String WRONG_PASSWORD = "회원 현재 비밀 번호 불일치";
    public static final String WRONG_CHECK_PASSWORD = "회원 확인 비밀 번호 불일치";
    public static final String SUCCESS_CHANGE_PW = "회원 비밀번호 수정 확인";


    public static final String READ_ALL_CONTENTS = "모든 컨텐츠 조회 성공";
    public static final String READ_CONTENT = "컨텐츠 조회 성공";
    public static final String NOT_FOUND_CONTENT = "컨텐츠가 존재하지 않습니다.";
    public static final String CREATE_CONTENT = "컨텐츠 작성 성공";
    public static final String FAIL_CREATE_CONTENT = "컨텐츠 작성 실패";
    public static final String UPDATE_CONTENT = "컨텐츠 수정 성공";
    public static final String FAIL_UPDATE_CONTENT = "컨텐츠 수정 실패";
    public static final String DELETE_CONTENT = "컨텐츠 삭제 성공";
    public static final String LIKE_CONTENT = "컨텐츠 좋아요/해제 성공";
    public static final String ARTWORK_NOPICUTRE = "그림이 없어요";
    public static final String NO_ARTWORKLIKE = "작품에 대한 좋아요 없음";
    public static final String READ_ALL_ARTWORKLIKE = "작품에 대한 좋아요 수 조회 성공";
    public static final String NOT_FOUND_PURCHASE = "구매 항목 미기입";
    public static final String CREATE_PURCHASE = "구매 정보 저장 성공";
    public static final String REFUND_REQUEST_SUCCESS = "환불 요청 성공";
    public static final String UNCOMPLETED_PURCHASE = "완료되지 않은 거래가 있어 작품을 삭제 대신 비활성화함";

    public static final String READ_ALL_COMMENTS = "모든 댓글 조회 성공";
    public static final String READ_COMMENT = "댓글 조회 성공";
    public static final String NOT_FOUND_COMMENT = "댓글이 존재하지 않습니다.";
    public static final String CREATE_COMMENT = "댓글 작성 성공";
    public static final String FAIL_CREATE_COMMENT = "댓글 작성 실패";
    public static final String UPDATE_COMMENT = "댓글 수정 성공";
    public static final String FAIL_UPDATE_COMMENT = "댓글 수정 실패";
    public static final String DELETE_COMMENT = "댓글 삭제 성공";
    public static final String FAIL_DELETE_COMMENT = "댓글 삭제 실패";
    public static final String LIKE_COMMENT = "댓글 좋아요";
    public static final String UNLIKE_COMMENT = "댓글 좋아요 해제";
    public static final String FAIL_COMMENT = "댓글 좋아요 실패";
    public static final String FAIL_UNLIKE_COMMENT = "댓글 좋아요 해제 실패";
    public static final String INDEX_NOT_FOUNDED = "유저가 쓴 댓글이 없음";

    public static final String AUTHORIZED = "인증 성공";
    public static final String UNAUTHORIZED = "인증 실패";
    public static final String FORBIDDEN = "인가 실패";

    public static final String INTERNAL_SERVER_ERROR = "서버 내부 에러";
    public static final String SERVICE_UNAVAILABLE = "현재 서비스를 사용하실 수 없습니다. 잠시후 다시 시도해 주세요.";

    public static final String DB_ERROR = "데이터베이스 에러";


    /* display - 다영 */
    public static final String READ_ALL_DISPLAY = "전시장 조회 성공";
    public static final String READ_DISPLAY = "전시 입장 성공";
    public static final String NOT_FOUND_DISPLAY = "전시회가 존재하지 않습니다. display_idx를 확인하세요.";
    public static final String NOT_FOUND_DISPLAYCONTENT = "전시 작품이 존재하지 않습니다.";
    public static final String FAIL_READ_DISPLAY = "전시회가 존재하지 않습니다.";
    public static final String CREATE_DISPLAY = "전시 등록 성공";
    public static final String FAIL_CREATE_DISPLAY = "전시와 작품을 선택해주세요.";
    public static final String FAIL_ALREADY_CREATE= "이미 전시를 등록하였습니다.";
    public static final String READ_DISPLAY_APPLICATION = "전시 신청서 조회 성공";
    public static final String DELETE_DISPLAY= "전시 취소 성공";
    public static final String NOT_FOUND_DISPLAY_APPLY= "전시 신청 내역이 존재하지 않습니다.";
    public static final String NOT_FOUND_READ_BUYS= "구매내역이 존재하지 않습니다.";
    public static final String NOT_FOUND_READ_SELLS= "판매내역이 존재하지 않습니다.";
    public static final String UPDATE_DISPLAY = "전시회 수정 성공";

    /* notice */
    public static final String READ_BUYS = "알림 구매 내역 조회 성공";
    public static final String FAIL_READ_BUYS = "알림 구매 내역 조회 실패";

    public static final String READ_SELLS = "알림 판매 내역 조회 성공";
    public static final String FAIL_READ_SELLS = "알림 판매 내역 조회 실패";


    public static final String READ_DISPLAY_APPLY = "전시 내역 조회 성공";
    public static final String FAIL_DISPLAY_APPLY = "전시 내역 조회 실패";


}
