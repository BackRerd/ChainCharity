package site.backrer.backed.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//vUNBa4YX9zGTZGjM
@Configuration
public class GlobalCorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns(
                                "http://cubeserver.w1.luyouxia.net",  // 你的前端域名
                                "http://localhost:*",                 // 本地开发
                                "http://127.0.0.1:*",                 // 本地IP访问
                                "http://110.42.36.248:*"              // 你的服务器IP
                        ).allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .maxAge(3600);
            }
        };
    }
}