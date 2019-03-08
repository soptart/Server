package org.sopt.artoo.mapper;

import org.apache.ibatis.annotations.*;
import org.sopt.artoo.dto.User;
import org.sopt.artoo.model.LoginReq;
import org.sopt.artoo.model.UserDescriptionReq;
import org.sopt.artoo.model.UserPwInfo;
import org.sopt.artoo.model.UserSignUpReq;

@Mapper
public interface UserMapper {

    /**
     * 회원 가입
     *
     * @param userSignUpReq 객체
     */
    @Insert("INSERT INTO user(u_email, u_pw, u_school, u_address, u_name, u_bank, u_account, u_phone) " +
            "VALUES(#{userSignUpReq.u_email}, #{userSignUpReq.u_pw}, #{userSignUpReq.u_school}, " +
            "#{userSignUpReq.u_address}, #{userSignUpReq.u_name}, #{userSignUpReq.u_bank}, #{userSignUpReq.u_account}, #{userSignUpReq.u_phone})")
    @Options(useGeneratedKeys = true, keyColumn = "user.u_idx")
    int save(@Param("userSignUpReq") final UserSignUpReq userSignUpReq);

    /**
     * 이메일 중복 검사
     *
     * @param u_email 유저 이메일
     * @return 유저 객체
     */
    @Select("SELECT * FROM user WHERE u_email = #{u_email}")
    User findByEmail(@Param("u_email") final String u_email);

    /**
     * 아이디로 이름 검색
     * @param userIdx 유저 인덱스
     * @return 유저 객체
     */
    @Select("SELECT * FROM user WHERE u_idx = #{userIdx}")
    User findByUidx(@Param("userIdx") final int userIdx);

    @Update("UPDATE user SET u_description = #{userDescriptionReq.u_description} WHERE u_idx = #{userIdx}")
    void saveUserDescription(@Param("userIdx") final int userIdx, @Param("userDescriptionReq") final UserDescriptionReq userDescriptionReq);

    /**
     * 이메일와 비밀번호로 조회
     * @return String 유저 이름
     */
    @Select("SELECT * FROM user WHERE u_email = #{loginReq.u_email} AND u_pw = #{loginReq.u_pw}")
    User findByIdAndPassword(@Param("loginReq") final LoginReq loginReq);

    /**
     * 유저 객체 수정
     * @param u_idx
     * @param userInfo
     */
    @Update("UPDATE user SET u_name = #{userInfo.u_name}, u_phone = #{userInfo.u_phone}, u_address = #{userInfo.u_address}," +
            "u_school = #{userInfo.u_school}, u_bank = #{userInfo.u_bank}, u_account = #{userInfo.u_account}, u_description = #{userInfo.u_description} WHERE u_idx = #{u_idx}")
    void updateUserInfo(@Param("u_idx") final int u_idx, @Param("userInfo") final User userInfo);

    /**
     * 유저 비밀번호 호출
     * @param u_idx
     * @return u_pw
     */
    @Select("SELECT u_pw FROM user WHERE u_idx = #{u_idx}")
    String checkUserPw(@Param("u_idx") final int u_idx);

    /**
     * 유저 비밀번호 업데이트
     * @param u_idx
     * @param userPwInfo
     */
    @Update("Update user SET u_pw = #{userInfo.u_pw_new} WHERE u_idx = #{u_idx}")
    void updateUserPw(@Param("u_idx") final int u_idx, @Param("userInfo") final UserPwInfo userPwInfo);
}
