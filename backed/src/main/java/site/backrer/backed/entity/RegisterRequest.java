package site.backrer.backed.entity;

import lombok.Data;

@Data
public class RegisterRequest {
    String username;
    String password;
    String phone;
}
