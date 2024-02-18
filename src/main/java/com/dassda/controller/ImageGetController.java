package com.dassda.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/items/{filename}")
public class ImageGetController {

    @GetMapping()
    public ResponseEntity<Resource> getImage(@PathVariable(value = "filename") String filename) throws MalformedURLException {
        Path filePath = Paths.get("/app/items" + filename);
        Resource resource = new UrlResource(filePath.toUri());
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }
}
