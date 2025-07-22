package site.backrer.backed.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.backrer.backed.utils.Result;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试Controller - 用于验证CORS配置
 */
@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/cors")
    public Result testCors() {
        Map<String, Object> data = new HashMap<>();
        data.put("message", "CORS配置正常");
        data.put("timestamp", System.currentTimeMillis());
        return Result.success(data);
    }

    @GetMapping("/health")
    public Result health() {
        return Result.success("服务正常运行");
    }
} 