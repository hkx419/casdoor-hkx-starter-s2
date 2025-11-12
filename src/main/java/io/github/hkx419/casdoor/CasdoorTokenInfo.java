package io.github.hkx419.casdoor;

import lombok.Data;

@Data
public class CasdoorTokenInfo {
    private String token;      // Casdoor token
    private String username;   // casdoorUser.name
    private Object rawUser;    // SDK 的完整 User 对象，可选
}

