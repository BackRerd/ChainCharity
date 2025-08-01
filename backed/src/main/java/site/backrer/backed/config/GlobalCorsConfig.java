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
                registry.addMapping("/**").
                        allowedOriginPatterns("*")  // 允许所有来源
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .maxAge(3600);
            }
        };
    }
//    allowedOriginPatterns(
//                                "http://cubeserver.w1.luyouxia.net",
//                                        "http://localhost:*",
//                                        "http://127.0.0.1:*",
//                                        "http://110.42.36.248:*",
//                                        "http://192.168.1.103:*",
//                                        "http://0.0.0.0:*",
//                                        "https://localhost:*",
//                                        "https://127.0.0.1:*"
//    )
}