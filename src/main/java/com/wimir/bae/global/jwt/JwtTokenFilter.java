package com.wimir.bae.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wimir.bae.domain.jwt.service.JwtService;
import com.wimir.bae.global.dto.ResponseDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 모든 HTTP 요청마다 실행
public class JwtTokenFilter extends OncePerRequestFilter {
    
    // JWT
    // [Header].[Payload].[Signature]
    // Payload는 토큰에 담을 데이터(Claims)를 뜻함
    // 키-값
    /*  Claims claims = Jwts.claims();
        claims.setSubject("user123");                // 토큰 주체
        claims.put("role", "ADMIN");                 // 커스텀 클레임
        claims.put("userKey", "abcde-12345");  */      // 원하는 데이터 넣기

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 요청헤더에서 "Authorization" 값 가져오기
        String jwt = request.getHeader("Authorization");

        // "Bearer " 부분 제거
        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7);

            try {
                // JWT에서 Claims(페이로드)를 가져옴
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(jwtService.getSecretKey())
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();

                String username = claims.getSubject();
                Authentication auth = new UsernamePasswordAuthenticationToken(username, null, AuthorityUtils.NO_AUTHORITIES);
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception e) {

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.setStatus(HttpServletResponse.SC_OK);

                ResponseDTO responseDTO = ResponseDTO.builder()
                        .result(-1)
                        .message("세션이 만료되었습니다. 계속하려면 다시 로그인하세요.")
                        .build();

                String jsonResponse = new ObjectMapper().writeValueAsString(responseDTO);
                response.getWriter().write(jsonResponse);
                return;

            }
        }
        filterChain.doFilter(request, response);
    }
}
