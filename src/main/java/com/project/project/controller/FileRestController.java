package com.project.project.controller;

import com.project.project.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class FileRestController {


    private final FileService fileService;

    // 의존성 주입
    public FileRestController (FileService fileService){
        this.fileService = fileService;
    }

    // 게시물 파일 제공 API --> 사실상 경로
    @GetMapping("/uploads/{filename}")
    public ResponseEntity<Resource> upload(@PathVariable String filename) {

        // Resource : 파일, 데이터 래핑하는 인터페이스 , FileSystemResource 클래스로 구현
        // 파일 조회 및 응답
        Resource resource = fileService.getPostFile(filename);

        // 파일 존재하지 않을 경우
        if(resource == null){
            return ResponseEntity.notFound().build();
        }

        // HTTPHeaders.CONTENT_DISPOSITION : HTTP 헤더에 Content-Disposition 설정 -> 클라리언트에게 해당 응답이 파일 다운로드임을 알림
        // "attachment; filename=\"" + resource.getFilename() + "\"" -> 브라우저가 파일을 강제 다운로드 함
        // 파일이 존재하는 경우
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);

    }

    // 메뉴 뉴스 파일 제공 API
    @GetMapping("/news/{filename}")
    public ResponseEntity<Resource> newsFile(@PathVariable String filename) {

        Resource resource = fileService.getNewsFile(filename);

        if(resource == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }





}
