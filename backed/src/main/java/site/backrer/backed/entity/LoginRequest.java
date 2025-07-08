package site.backrer.backed.entity;

import lombok.Data;

@Data
public class LoginRequest {
    String username;
    String password;
}
