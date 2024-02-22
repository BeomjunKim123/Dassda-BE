package com.dassda.controller;

import jakarta.servlet.ServletContext;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class ImageGetController {

    private final ServletContext servletContext;

    @GetMapping("/items/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable(value = "filename") String filename) throws MalformedURLException {
        Path filePath = Paths.get("/root/items/" + filename);
        Resource resource = new UrlResource(filePath.toUri());
        String mimeType = servletContext.getMimeType(filePath.toString());
        if (mimeType == null) {
            // 기본값으로 설정
            mimeType = "application/octet-stream";
        }

        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .body(resource);
    }
}
