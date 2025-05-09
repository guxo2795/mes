package com.wimir.bae.domain.user.controller;

import com.wimir.bae.domain.user.dto.*;
import com.wimir.bae.domain.user.service.UserService;
import com.wimir.bae.global.dto.CountDTO;
import com.wimir.bae.global.dto.ListWrapperDTO;
import com.wimir.bae.global.dto.ResponseDTO;
import com.wimir.bae.global.jwt.JwtGlobalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "01. 기준정보관리 > 01. 사원 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping("user")
public class UserController {

    private final JwtGlobalService jwtGlobalService;
    private final UserService userService;

    @Operation(summary = "사원 등록", description = "사원 등록 API")
    @PostMapping("create")
    public ResponseEntity<ResponseDTO<?>> createUser(
            @RequestHeader("Authorization") String accessToken,
            @RequestPart(value = "json") @Valid UserRegDTO regDTO,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestPart(value = "signatureFile", required = false) MultipartFile signatureFile) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        userService.createUser(userLoginDTO, regDTO, imageFile, signatureFile);

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리되었습니다.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("list")
    public ResponseEntity<ResponseDTO<List<UserInfoDTO>>> getUserList(
            @RequestHeader("Authorization") String accessToken,
            @ModelAttribute @Valid UserSearchDTO searchDTO) {
        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        List<UserInfoDTO> list = userService.getUserList(userLoginDTO, searchDTO);

        ResponseDTO<List<UserInfoDTO>> responseDTO =
                ResponseDTO.<List<UserInfoDTO>>builder()
                        .result(1)
                        .data(list)
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("count")
    public ResponseEntity<ResponseDTO<CountDTO>> getUserCount(
            @RequestHeader("Authorization") String accessToken,
            @ModelAttribute @Valid UserSearchDTO searchDTO) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        CountDTO count = userService.getUserCount(userLoginDTO, searchDTO);

        ResponseDTO<CountDTO> response =
                ResponseDTO.<CountDTO>builder()
                        .result(1)
                        .data(count)
                        .build();

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("update")
    public ResponseEntity<ResponseDTO<?>> updateUser(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid UserModDTO modDTO) {
        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        userService.updateUser(userLoginDTO, modDTO);

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리 되었습니다.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("delete")
    public ResponseEntity<ResponseDTO<?>> deleteUser(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid ListWrapperDTO<String> userKeyList) {
        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        userService.deleteUser(userLoginDTO, userKeyList.getList());

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리 되었습니다.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("password/update")
    public ResponseEntity<ResponseDTO<?>> updatePassword(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid UserUpdatePasswordDTO userUpdatePasswordDTO) {
        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        userService.updatePassword(userLoginDTO, userUpdatePasswordDTO);

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리 되었습니다.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

}
