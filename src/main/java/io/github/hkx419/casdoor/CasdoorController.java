package io.github.hkx419.casdoor;

import io.swagger.v3.oas.annotations.Operation;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.casbin.casdoor.entity.User;
import org.casbin.casdoor.service.AuthService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.oauth2.common.OAuth2AccessToken;


import java.io.IOException;
import java.util.UUID;

@RestController
@ConditionalOnProperty(prefix = "casdoor", name = "endpoint")

public class CasdoorController {
    private final UserLoginHandler userLoginHandler;


    private final AuthService authService;
    private final CasdoorProperties properties;

    public CasdoorController(UserLoginHandler userLoginHandler, AuthService authService, CasdoorProperties properties) {
        this.userLoginHandler = userLoginHandler;
        this.authService = authService;
        this.properties = properties;
    }


    @GetMapping("/login/casdoor")
    @Operation(summary = "casdoor登录", description = "casdoor")
    public Response<String> loginCasdoor(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 生成随机 state 并存入 session
        String state = UUID.randomUUID().toString();
        request.getSession().setAttribute("oauthState", state);
        // 获取 Casdoor 授权 URL
        String signinUrl = authService.getSigninUrl(properties.getRedirectUri(), state);
        // 直接跳转到 Casdoor 登录页面
        // 返回 URL 给前端，由前端自己跳转
        System.out.println("signinUrl: " + signinUrl);
        return Response.ok(signinUrl, "获取 Casdoor 登录地址成功");
    }


    @GetMapping("/login/token")
    public Response<OAuth2AccessToken> casdoorCallback(String code, String state, HttpSession session, HttpServletResponse response) throws IOException {
        String sessionState = (String) session.getAttribute("oauthState");
        System.out.println("获取到的state： "+ sessionState );
        if (sessionState == null || !sessionState.equals(state)) {
            throw new IllegalArgumentException("非法请求，state 不匹配！");
        }
        session.removeAttribute("oauthState");
        // 1. 获取 Casdoor token 并解析用户
        String casdoorToken = authService.getOAuthToken(code, state);
        System.out.println("获取到 token: " + casdoorToken);
        User casdoorUser = authService.parseJwtToken(casdoorToken);

        // 2. 调用业务系统登录逻辑
        try {
           Response<OAuth2AccessToken> result = userLoginHandler.loginByUsername(casdoorUser.name);
            if (result.getCode() == 0 && result.getData() != null) {
                return Response.ok(result.getData(), "登录成功");
            } else {
                return Response.error("登录失败: " + result.getMsg());
            }

        } catch (RuntimeException e) {
            return Response.error("登录异常: " + e.getMessage());
        }
     }
}

