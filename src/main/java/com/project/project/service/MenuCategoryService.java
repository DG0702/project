package com.project.project.service;

import com.project.project.entity.MenuCategory;
import com.project.project.repository.MenuCategoryRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
public class MenuCategoryService {

    @Autowired
    private MenuCategoryRepository menuCategoryRepository;
    @Autowired
    private FileService fileService;

    @Transactional
    public MenuCategory updateNews(Long menuId, String title, String contents, MultipartFile [] files, HttpSession session) {

        // 메뉴 아이디 찾기
        MenuCategory existingMenuId = menuCategoryRepository.findById(menuId).orElse(null);
        
        if(existingMenuId == null){
            throw new IllegalArgumentException("메뉴를 찾을 수 없습니다");
        }

        // 제목, 내용 변경
        existingMenuId.setTitle(title);
        existingMenuId.setContents(contents);

        // 파일 존재시
        try {
            if(files != null && files.length > 0){
                fileService.updateNews(menuId,files,session);
            }
        } catch (IOException e) {
            throw new RuntimeException("메뉴 첨부파일 처리 중 오류",e);
        }
        return menuCategoryRepository.save(existingMenuId);
    }
}
