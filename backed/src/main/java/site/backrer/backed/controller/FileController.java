package site.backrer.backed.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class FileController {

    @Value("${file.upload-dir.image}")
    private String imageUploadDir;

    @Value("${file.upload-dir.file}")
    private String fileUploadDir;

    // 图片上传接口
    @PostMapping("/upload/image")
    public ResponseEntity<String> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "userId", required = false) String userId) throws IOException {

        if (file.isEmpty()) return ResponseEntity.badRequest().body("Empty file");

        String filename = UUID.randomUUID() + "-" + StringUtils.cleanPath(file.getOriginalFilename());

        // 路径拼接
        Path uploadPath = (userId != null && !userId.isBlank())
                ? Paths.get(imageUploadDir, userId)
                : Paths.get(imageUploadDir);
        uploadPath = uploadPath.toAbsolutePath();

        File dest = uploadPath.resolve(filename).toFile();
        dest.getParentFile().mkdirs(); // 自动创建目录
        file.transferTo(dest);

        // 返回访问路径
        String imageUrl = (userId != null && !userId.isBlank())
                ? "/images/" + userId + "/" + filename
                : "/images/" + filename;

        return ResponseEntity.ok(imageUrl);
    }

    // 文件上传接口
    @PostMapping("/upload/file")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "userId", required = false) String userId) throws IOException {

        if (file.isEmpty()) return ResponseEntity.badRequest().body("Empty file");

        String filename = UUID.randomUUID() + "-" + StringUtils.cleanPath(file.getOriginalFilename());

        Path uploadPath = (userId != null && !userId.isBlank())
                ? Paths.get(fileUploadDir, userId)
                : Paths.get(fileUploadDir);
        uploadPath = uploadPath.toAbsolutePath();

        File dest = uploadPath.resolve(filename).toFile();
        dest.getParentFile().mkdirs();
        file.transferTo(dest);

        return ResponseEntity.ok("File uploaded: " + filename);
    }

    // 获取默认图片
    @GetMapping(value = "/images/{filename:.+}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<FileSystemResource> getDefaultImage(@PathVariable String filename) {
        File image = Paths.get(imageUploadDir).toAbsolutePath().resolve(filename).toFile();
        if (!image.exists()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(new FileSystemResource(image));
    }

    // 获取用户图片
    @GetMapping(value = "/images/{userId}/{filename:.+}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<FileSystemResource> getUserImage(@PathVariable String userId, @PathVariable String filename) {
        File image = Paths.get(imageUploadDir, userId).toAbsolutePath().resolve(filename).toFile();
        if (!image.exists()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(new FileSystemResource(image));
    }

    // 获取文件（统一下载）
    @GetMapping("/files/{userId}/{filename:.+}")
    public ResponseEntity<FileSystemResource> getUserFile(@PathVariable String userId, @PathVariable String filename) {
        File file = Paths.get(fileUploadDir, userId).toAbsolutePath().resolve(filename).toFile();
        if (!file.exists()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"")
                .body(new FileSystemResource(file));
    }
}
