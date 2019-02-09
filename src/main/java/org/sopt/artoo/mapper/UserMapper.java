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
    @Insert("INSERT INTO user(u_email, u_pw, u_school, u_name, u_phone) " +
            "VALUES(#{userSignUpReq.u_email}, #{userSignUpReq.u_pw}, #{userSignUpReq.u_school}, " +
            "#{userSignUpReq.u_name}, #{userSignUpReq.u_phone})")
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

    /**
     * 회원 소개란 수정
     * @param userIdx
     * @param userDescriptionReq
     */
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
    @Update("UPDATE user SET u_name = #{userInfo.u_name}, u_phone = #{userInfo.u_phone}, " +
            "u_school = #{userInfo.u_school} WHERE u_idx = #{u_idx}")
    void updateUserInfo(@Param("u_idx") final int u_idx, @Param("userInfo") final User userInfo);

    /**
     * 유저 비밀번호 호출
     * @param u_idx
     * @return u_pw
     */
    @Select("SELECT u_pw FROM user WHERE u_idx = #{u_idx}")
    String checkUserPw(@Param("u_idx") final int u_idx);

    //유저 아이디와 회원가입타입으로 회원 조회
    @Select("SELECT * FROM user WHERE external_key = #{external_key} AND u_type = #{u_type}")
    User findByUserIdAndType(@Param("external_key") final int external_key, @Param("u_type") final int u_type);
    /**
     * 유저 비밀번호 업데이트
     * @param u_idx
     * @param userPwInfo
     */
    @Update("Update user SET u_pw = #{userInfo.u_pw_new} WHERE u_idx = #{u_idx}")
    void updateUserPw(@Param("u_idx") final int u_idx, @Param("userInfo") final UserPwInfo userPwInfo);

    /**
     * 유저 정보 비활성화
     * @param u_idx
     */
    @Update("Update user SET u_pw = '3BE481CA29E74A01367CEACA0B5C7F5EE53E9A407D26D4368EDD539541F7B13C', " +
            "u_school ='', u_name='', u_phone='', u_description='' WHERE u_idx = #{u_idx}")
    void inActiveUser(@Param("u_idx") final int u_idx);

}
