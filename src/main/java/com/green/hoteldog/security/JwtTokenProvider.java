package com.green.hoteldog.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.hoteldog.common.AppProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

import static io.jsonwebtoken.Jwts.claims;

@Slf4j
@Component//Bean등록
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final ObjectMapper objectMapper;
    private final AppProperties appProperties;
    private SecretKeySpec secretKeySpec;
    /*
    -FE 아이디,비번 BE로 전달
    -BE 아이디,비번 인증처리
    -AT,RT 발행
    -AT,RT 안에는 MyPrincipal(userPk) 객체 내용이 포함
    -Response Body에 AT를 담아 리턴, RT는 Cookie에 담는다
    -FE Request를 보낼 때 Header에 Authorization 키 값에 Bearer $(AT)를 담아서 보낸다.
    -BE Request에 Authorization 키값이 있는지 확인, 없으면 통과
    -있으면 Token값을 빼내서, Authentication 객체를 만들어서
     SecurityContextHolder 안의 SecurityContext 객체 안에 Authentication 객체 주소값을 담는다.
    -BE SecurityContext 객체 안에 Authentication 객체 주소값이 있으면 로그인 되었다라고 판단, 없으면 비로그인 상태라고 판단.
    */
    @PostConstruct//DI가 이루어진 후 최초 한번 호출하는 것.
    public void init(){
        this.secretKeySpec = new SecretKeySpec(appProperties.getJwt().getSecret().getBytes()
                , SignatureAlgorithm.HS256.getJcaName());

    }
    public String generateAccessToken(Myprincipal myprincipal){
        return generateToken(myprincipal,appProperties.getJwt().getAccessTokenExpiry());
    }
    public String generateRefreshToken(Myprincipal myprincipal){
        return generateToken(myprincipal,appProperties.getJwt().getRefreshTokenExpiry());
    }

    private String generateToken(Myprincipal myprincipal,long toeknValidMs){
        Date now = new Date();
        return Jwts.builder()
                .claims(createClaims(myprincipal))
                .issuedAt(new Date(System.currentTimeMillis()))//발행 시간 등록
                //.issuedAt(now)
                .expiration(new Date(System.currentTimeMillis() + toeknValidMs))//만료 시간 등록
                .signWith(secretKeySpec)
                .compact();
    }
    public boolean isValidateToken(String token){
        try {
            return !getAllClaims(token).getExpiration().before(new Date());
            //만료기간이 현재시간보다 전이면 false 후면 true.
        }catch (Exception e){
            return false;
        }
    }
    private Claims getAllClaims(String token){
        return Jwts
                .parser()
                .verifyWith(secretKeySpec)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    private Claims createClaims(Myprincipal myprincipal){

        try{
            String json = objectMapper.writeValueAsString(myprincipal);
            return claims()
                    .add("user",json)
                    .build();
        }catch (Exception e){
            return null;
        }

    }
    public Authentication getAuthentication(String token){
        UserDetails userDetails = getUserDetailsFromToken(token);

        return userDetails == null ? null : new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());
    }
    public UserDetails getUserDetailsFromToken(String token){
        try {
            Claims claims = getAllClaims(token);
            String json = (String)claims.get("user");
            Myprincipal myprincipal = objectMapper.readValue(json, Myprincipal.class);
            return MyUserDtails.builder()
                    .myprincipal(myprincipal)
                    .build();
        }catch (Exception e){
            return null;
        }
    }

    public String resolveToken(HttpServletRequest req){
        String auth = req.getHeader(appProperties.getJwt().getHeaderSchemeName());
        if(auth == null){
            return null;
        }
        if(auth.startsWith((appProperties.getJwt().getTokenType()))){
            return auth.substring(appProperties.getJwt().getTokenType().length()).trim();
            //.tirm() : 문자열 앞,뒤 공백 제거
        }
        return null;
    }
}
