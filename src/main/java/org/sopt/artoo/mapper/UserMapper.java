package org.sopt.artoo.mapper;

import org.apache.ibatis.annotations.*;
import org.sopt.artoo.dto.User;
import org.sopt.artoo.model.UserSignUpReq;

@Mapper
public interface UserMapper {

    //회원 가입, 모든 케이스가 다 채워진 경우
    @Insert("INSERT INTO user(u_email, u_pw, u_phoneNum, u_address, u_name, u_bank, u_account) " +
            "VALUES(#{userSignUpReq.u_email}, #{userSignUpReq.u_pw}, #{userSignUpReq.u_phoneNum}, #{userSignUpReq.u_address}, " +
            "#{userSignUpReq.u_name}, #{userSignUpReq.u_bank}, #{userSignUpReq.u_account}")
    @Options(useGeneratedKeys = true, keyColumn = "user.u_idx")
    int save(@Param("userSignUpReq") final UserSignUpReq userSignUpReq);

    //아이디 중복 검사
    @Select("SELECT * FROM user WHERE email = #{email}")
    User findByEmail(@Param("email") final String email);

    @Select("SELECT u_name FROM user WHERE u_idx = #{userIdx}")
    String findUnameByUidx(@Param("userIdx") final int userIdx);
}
