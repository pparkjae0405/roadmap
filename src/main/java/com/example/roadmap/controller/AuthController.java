package com.example.roadmap.controller;

import com.example.roadmap.dto.UserDTO;
import com.example.roadmap.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // Restuful 웹서비스의 컨트롤러, Json 형태로 객체 데이터를 반환
@RequiredArgsConstructor // final 혹은 @NotNull이 붙은 필드의 생성자를 자동으로 만들어준다
public class AuthController {
    private final AuthService authService;

    /**
     * 소셜 로그인 ( GET /login/oauth2/code/{registrationId}?code={code} )
     */
    @GetMapping(value = "/login/oauth2/code/{registrationId}")
    public ResponseEntity socialLogin(@PathVariable String registrationId,
                                    @RequestParam("code") String code,
                                      HttpServletResponse response) {
        return ResponseEntity.ok(authService.socialLogin(registrationId, code, response));
    }

    /**
     * 회원 가입 ( POST /signup )
     */
    @PostMapping(value = "/signup")
    public ResponseEntity save(@RequestBody UserDTO.Request dto,
                               HttpServletResponse response) {
        return ResponseEntity.ok(authService.save(dto, response));
    }

    /**
     * 토큰 재발급 ( GET /reissue )
     */
    @GetMapping(value = "/reissue")
    public ResponseEntity reissueToken(@CookieValue(value = "RefreshToken") String refreshToken) {
        return ResponseEntity.ok(authService.reissueToken(refreshToken));
    }
}
