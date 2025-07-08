package site.backrer.backed.entity;

import lombok.Data;

@Data
public class UserInfoResponse {
    Integer userId;
    String name;
    String email;
    Integer role;

    public UserInfoResponse(Integer userId, String name, String email, Integer role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
    }
}
