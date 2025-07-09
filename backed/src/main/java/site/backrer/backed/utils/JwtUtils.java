package site.backrer.backed.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import site.backrer.backed.entity.UserInfoResponse;
import site.backrer.backed.entity.Users;

import java.util.Date;

@Component
public class JwtUtils {
    private final String jwtSecret = "dK0F4-+9aO0oPTQe2bmEYZ5QbL9S3t7whglhklKY5G4="; // 建议放到配置文件
    private final long jwtExpirationMs = 86400000; // 1天
    public String generateToken(Users user) {
        return Jwts.builder()
                .setSubject(user.getUsername())      // 用户名
                .claim("id", user.getUserId())           // 用户ID
                .claim("name",user.getUsername())
                .claim("phone",user.getPhone())
                .claim("role", user.getIsAdmin())       // 用户身份
                .setIssuedAt(new Date())             // 签发时间
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs)) // 过期时间
                .signWith(SignatureAlgorithm.HS512, jwtSecret) // 签名
                .compact();
    }
    public UserInfoResponse getUserInfoFromJwt(String token) {
        if (!validateJwt(token)){
            return null;
        }
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        // 从claims中获取用户信息
        Integer id = (Integer) claims.get("id");
        String username = claims.getSubject();
        Integer role = (Integer) claims.get("role");
        String email = (String) claims.get("phone");

        return new UserInfoResponse(id, username, email,role); // 返回封装的用户信息
    }
    public boolean validateJwt(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
    // 从 Token 获取用户名
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}