package com.green.hoteldog.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {
    public MyUserDtails getLoginUser(){
        try {
            return (MyUserDtails)SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getPrincipal();
        }catch (Exception e){
            return null;
        }
    }
    public int getLoginUserPk(){
        MyUserDtails myUserDtails = getLoginUser();
        return myUserDtails == null ? 0 : myUserDtails.getMyprincipal().getUserPk();
    }
}
