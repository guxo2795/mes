package com.wimir.bae.domain.jwt.service;

import com.wimir.bae.domain.jwt.dto.JwtLoginDTO;
import com.wimir.bae.domain.user.dto.UserLoginInfoDTO;
import com.wimir.bae.domain.user.mapper.UserMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Value("${bae.jwt.key}")
    private String fixedSecretekey;
    @Getter
    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(fixedSecretekey.getBytes(StandardCharsets.UTF_8));
    }

    // 로그인
    public String login(JwtLoginDTO jwtLoginDTO) {

        String userCode = jwtLoginDTO.getUserCode();
        String password = jwtLoginDTO.getPassword();
        String userKey = userMapper.getUserKeyByUserCode(jwtLoginDTO.getUserCode());

        // 유저 아이디 확인
        if (userKey.isBlank())
            throw new IllegalArgumentException("존재하지 않는 아이디입니다.");

        // 비밀번호 확인
        String currentPassword = userMapper.getPasswordByUserKeyAndUserCode(userKey, userCode);
        if (!verifyPassword(password, currentPassword))
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");


        // 로그인 성공시 액세스 토큰이 아닌 리프레시 토큰을 발급
        String refreshToken = createRefreshToken(userCode);
        // 만료 날짜 얻기 => getRefreshToeknExpiration(refreshToken)으로 바꾸는게 맞나?
        Date extiredDate = getAccessTokenExpiration(refreshToken);
        // 날짜 포맷
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String expiredDateString = format.format(extiredDate);

        try {
            userMapper.updateUserTokenDate(userKey, expiredDateString, userCode);
        } catch (Exception e) {
            throw new IllegalArgumentException("서버 오류입니다. 관리자에게 문의하세요.");
        }

        return refreshToken;
    }

    // 액세스 토큰 발급
    public String silent(String refreshToken) {

        if(refreshToken == null) throw new IllegalArgumentException("토큰이 없습니다.");
        if(refreshToken.startsWith("Bearer")) refreshToken = refreshToken.substring(7);

        try {
            String userCode = getUserCodeFromToken(refreshToken);
            String userKey = userMapper.getUserKeyByUserCode(userCode);
            Date expiredDate = getRefreshTokenExpiration(refreshToken);
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String expiredDateString = format.format(expiredDate);

            UserLoginInfoDTO userLoginInfoDTO = userMapper.getUserTokenDate(userKey, expiredDateString);
            if(userLoginInfoDTO == null) throw new IllegalArgumentException("유효하지 않은 토큰입니다.");

            return createAccessToken(userCode);
        } catch (Exception e) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

    }


    // 액세스 토큰 생성
    public String createAccessToken(String userCode) {
        Claims claims = Jwts.claims().setSubject(userCode);
        Date now = new Date();

        long tokenValidTime = 1000L * 60 * 60 * 2; // 2시간

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // 리프레시 토큰 생성
    private String createRefreshToken(String userCode) {
        Claims claims = Jwts.claims().setSubject(userCode);
        Date now = new Date();

        long refreshTokenValidTime = 1000L * 60 * 60 * 24 * 30; // 30일

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰에서 아이디 얻기
    public String getUserCodeFromToken(String token) {
        if(token == null || token.isBlank()) throw new IllegalArgumentException("토큰이 없습니다.");

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
    }


    // 토큰 만료날짜 얻기
    private Date getTokenExpirationDate(String token, String errorMessage) {
        if (token == null || token.isBlank())
            throw new IllegalArgumentException("토큰이 없습니다.");

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();
        } catch (Exception e) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public Date getAccessTokenExpiration(String token) {
        return getTokenExpirationDate(token, "세션이 만료되었습니다. 계속하러면 다시 로그인 하세요.");
    }

    public Date getRefreshTokenExpiration(String token) {
        return getTokenExpirationDate(token, "세션이 만료되었습니다. 자동 로그아웃 됩니다.");
    }


    public boolean verifyPassword(String password, String currentPassword) {
        return passwordEncoder.matches(password, currentPassword);
    }

}
