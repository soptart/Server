package org.sopt.artoo.service;

import lombok.extern.slf4j.Slf4j;
import org.sopt.artoo.dto.User;
import org.sopt.artoo.mapper.UserMapper;
import org.sopt.artoo.model.DefaultRes;
import org.sopt.artoo.model.UserSignUpReq;
import org.sopt.artoo.utils.ResponseMessage;
import org.sopt.artoo.utils.StatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Slf4j
@Service
public class UserService {

    private final UserMapper userMapper;

    public UserService(final UserMapper userMapper){
        this.userMapper = userMapper;
    }

    @Transactional
    public DefaultRes save(UserSignUpReq userSignUpReq) {
        if(userSignUpReq.checkQualification()) { //이메일 중복 검사
//            final User user = userMapper.findByEmail(userSignUpReq.getU_email());
//            if(user == null) {
                try {
                    userMapper.save(userSignUpReq);
                    return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_USER);
                } catch (Exception e) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    log.error(e.getMessage());
                    return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
                }
//            } else return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.ALREADY_USER);
        }
        return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.FAIL_CREATE_USER);
    }







}
