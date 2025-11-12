package io.github.hkx419.casdoor;

import org.springframework.security.oauth2.common.OAuth2AccessToken;

public interface UserLoginHandler {
    /**
     * 根据用户名执行业务系统登录逻辑
     * @param username Casdoor 登录用户名
     * @return true 表示登录成功，false 表示失败
     */
    Response<OAuth2AccessToken> loginByUsername(String username);
}