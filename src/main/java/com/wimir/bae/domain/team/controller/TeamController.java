package com.wimir.bae.domain.team.controller;

import com.wimir.bae.domain.team.dto.TeamRegDTO;
import com.wimir.bae.domain.team.service.TeamService;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.global.dto.ResponseDTO;
import com.wimir.bae.global.jwt.JwtGlobalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("team")
public class TeamController {

    private final JwtGlobalService jwtGlobalService;
    private final TeamService teamService;

    // 팀 등록
    @PostMapping("create")
    public ResponseEntity<ResponseDTO<?>> createTeam(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid TeamRegDTO regDTO) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");

        teamService.createTeam(regDTO, userLoginDTO);

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리 되었습니다")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

}
