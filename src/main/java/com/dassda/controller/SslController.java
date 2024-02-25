package com.dassda.controller;

import jakarta.servlet.Servlet;
import jakarta.servlet.ServletContext;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
@RequestMapping("/.well-known/pki-validation")
public class SslController {

    private final ServletContext servletContext;
    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable(value = "filename") String filename) throws IOException {
        if(filename.contains("..")) {
            // 잘못된 요청 처리
            return ResponseEntity.badRequest().build();
        }

        // 파일을 제공할 실제 경로를 `/var/www/html/.well-known/pki-validation/`로 변경
        Path filePath = Paths.get("/var/www/html/.well-known/pki-validation/" + filename);
        Resource resource = new UrlResource(filePath.toUri());
        if (!resource.exists() || !resource.isReadable()) {
            // 파일이 존재하지 않거나 읽을 수 없는 경우, 404 처리
            return ResponseEntity.notFound().build();
        }

        String mimeType = servletContext.getMimeType(resource.getFile().getAbsolutePath());
        if (mimeType == null) {
            // 파일의 MIME 타입을 결정할 수 없는 경우, 기본값으로 설정
            mimeType = "application/octet-stream";
        }

        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .body(resource);
    }
}
