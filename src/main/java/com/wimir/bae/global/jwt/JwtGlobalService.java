package com.wimir.bae.global.jwt;

import com.wimir.bae.domain.jwt.service.JwtService;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.domain.user.mapper.UserMapper;
import com.wimir.bae.global.exception.CustomAccessDenyException;
import com.wimir.bae.global.exception.CustomTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtGlobalService {

    private final JwtService jwtService;
    private final UserMapper userMapper;

    // 사원, 권한 확인
    public UserLoginDTO getTokenInfo(String accessToken, String  targetUserClass) {

        if(accessToken == null || accessToken.isEmpty())
            throw new CustomTokenException("토큰 값이 필요합니다.");

        if(accessToken.startsWith("Bearer "))
            accessToken = accessToken.substring(7);

        Date tokenDate = jwtService.getAccessTokenExpiration(accessToken);
        Date now = new Date();
        if(now.after(tokenDate)) throw new CustomTokenException("세션이 만료되었습니다. 계속하려면 다시 로그인하세요.");

        String userCode = jwtService.getUserCodeFromToken(accessToken);
        String userKey = userMapper.getUserKeyByUserCode(userCode);
        String userClass = userMapper.getUserClass(userKey);

        if(userClass == null) throw new CustomTokenException("존재하지 않는 사원입니다.");

        switch(targetUserClass) {
            case "R":
                if(!userClass.equals(targetUserClass)) throw new CustomAccessDenyException();
                break;
            case "A":
                if(!List.of("R","A").contains(userClass)) throw new CustomAccessDenyException();
                break;
            case "U":
                if(!List.of("R","A","U").contains(userClass)) throw new CustomAccessDenyException();
                break;
            default:
                    throw new CustomAccessDenyException();
        }

        return new UserLoginDTO(userCode, userClass);
    }

}
