package com.project.project.service;

import com.project.project.dto.AttachedFileDTO;
import com.project.project.entity.AttachedFile;
import com.project.project.entity.Board;
import com.project.project.entity.Member;
import com.project.project.entity.MenuCategory;
import com.project.project.repository.AttachedFileRepository;
import com.project.project.repository.BoardRepository;
import com.project.project.repository.MenuCategoryRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FileService {


    private final AttachedFileRepository attachedFileRepository;

    private final BoardRepository boardRepository;

    private final MenuCategoryRepository menuCategoryRepository;

    // 의존성 주입
    public FileService(AttachedFileRepository attachedFileRepository, BoardRepository boardRepository,
                       MenuCategoryRepository menuCategoryRepository) {
        this.attachedFileRepository = attachedFileRepository;
        this.boardRepository = boardRepository;
        this.menuCategoryRepository = menuCategoryRepository;
    }


    // 파일을 서버에 업로드하고 파일 정보를 데이터베이스에 저장하는 역할 

    // MultipartFile file >> 사용자가 업로드한 파일을 나타내는 객체
    public List<AttachedFile> uploadFile(MultipartFile [] files, AttachedFileDTO attachedFileData, HttpSession session) throws IOException {

        // 사용자 아이디 가져오기
        Member member = (Member) session.getAttribute("member");

        // 파일을 저장할 경로 설정
        String uploadDir= "C:\\project\\uploads\\";

        // 여러 파일을 담을 리스트
        List<AttachedFile> uploadFiles = new ArrayList<>();

        // 첨부파일이 여러 개일 경우
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {


                // 파일 저장 파일명 설정 (중복 방지를 위해 원본 파일명 앞에 현재시간밀리초로 표현)
                String savedFileName = System.currentTimeMillis() + file.getOriginalFilename();
                // 실제 파일이 저장될 경로를 지정하는 File 객체 생성
                File dest = new File(uploadDir + savedFileName);

                file.transferTo(dest);




                // 파일 정보 DTO 업데이트

                // 사용자 아이디
                attachedFileData.setUploadId(member.getUserId());
                // 경로를 파일명만 저장
                attachedFileData.setPath1(savedFileName);
                // 저장된 파일명
                attachedFileData.setSavedName(savedFileName);
                // file.getBytes() >> 업로드된 파일의 크기(byte 단위로 설정)
                // 파일크기는 long 타입으로 반환되므로 String 타입으로 변환하여 저장 >> 포맷을 쉽게 변환 가능하기 때문 >> 직관적
                attachedFileData.setFileSize(String.valueOf(file.getSize()));

                // DTO -> Entity 변환 후 저장
                AttachedFile attachedFile = attachedFileData.toEntity();
                // 데이터베이스 저장 후 저장된 엔티티 객체 반환 후 재할당
                attachedFile = attachedFileRepository.save(attachedFile);

                // 저장된 파일을 리스트 추가
                uploadFiles.add(attachedFile);
            }
        }

        return uploadFiles;


    }





    // 게시물에 첨부된 파일 목록 조회
    public List<AttachedFile> getFilesByBoarId(Long boarId) {
        return attachedFileRepository.findByBoardId(boarId);
    }






    // 게시물의 첨부파일을 제공하는 서비스 (HTTP 프로토콜로 파일 반환) -> filePath 기반으로 파일을 찾아서 응답

    public Resource getPostFile (String filename){
        try {

            // 파일을 저장한 디렉토리 경로
            String uploadDir= "C:\\project\\uploads\\";

            // 지정된 경로에 있는 파일 -> 객체를 생성
            File file = new File(uploadDir + filename);


            // 파일이 존재하지 않으면
            if(!file.exists()){
                return null;
            }

            // 파일이 존재하면 -> FileSystemResource 객체로 파일을 래핑하여 반환
            // FileSystemResource -> Resource 인터페이스 구현체 , 파일을 읽고 쓸수 있음
            return new FileSystemResource(file);
        }
        catch (Exception e){
            log.info(e.getMessage());
            return null;
        }
    }





    // 게시물 수정 시 파일 업데이트
    @Transactional
    public AttachedFile uploadFilesForUpdate(MultipartFile file, AttachedFileDTO attachedFileData ,HttpSession session) throws IOException{

        // 사용자 아이디 가져오기
        Member member = (Member) session.getAttribute("member");


        // 임시 디렉토리
        String tempDir = "C:\\project\\uploads\\tmp\\";
        // 최종 저장 디렉토리
        String uploadDir = "C:\\project\\uploads\\";


            // 파일이 비어 있지 않을 경우
            if(!file.isEmpty()) {

                String originalFileName = file.getOriginalFilename();


                // 파일 저장 파일명 설정 (중복 방지)
                String savedFileName = System.currentTimeMillis() + originalFileName;


                // 실제 임시 디렉토리에 저장될 경로
                File tempFile = new File(tempDir + savedFileName);


                // 임시 파일 저장
                try {
                    file.transferTo(tempFile);
                }
                catch (IOException e){
                    throw new RuntimeException("파일 업로드 오류 발생 ",e);
                }


                // 임시 파일을 최종 저장 디렉토리로 이동
                File finalFile = new File(uploadDir + savedFileName);
                if(tempFile.renameTo(finalFile)){


                    // 파일 정보 DTO 업데이트
                    attachedFileData.setUploadId(member.getUserId());
                    attachedFileData.setPath1(savedFileName);
                    attachedFileData.setSavedName(savedFileName);
                    attachedFileData.setFileSize(String.valueOf(finalFile.length()));



                    // DTO -> Entity 변환 후 저장
                    AttachedFile attachedFile = attachedFileData.toEntity();
                    attachedFile = attachedFileRepository.save(attachedFile);

                    // 저장된 파일을 리스트에 추가
                    return attachedFile;
                }else{
                    throw new RuntimeException("파일을 최종 디렉토리로 이동하는데 실패했습니다");
                }




            }


        return null;
    }




    // 게시물 수정시 기존 첨부파일 삭제 (데이터베이스, 서버)
    @Transactional
    public void deleteFile(Board existingBoard) throws IOException {
        for(AttachedFile attachedFile : existingBoard.getAttachedFileList()){


            try {
                // 첨부파일 테이블에서 삭제
                attachedFileRepository.delete(attachedFile);

                // 실제 서버에서 삭제
                // 저장 경로
                String uploadDir= "C:\\project\\uploads\\";
                // 서버에 저장된 파일 객체 생성
                File savedFile = new File(uploadDir + attachedFile.getSavedName());

                // 실제 파일 존재하는 경우에만 삭제
                if(savedFile.exists()){
                    savedFile.delete();
                }
            }
            catch (Exception e){
                throw new IOException("파일 삭제 중 오류가 발생했습니다",e);
            }




        }

        // 기존 게시물의 첨부파일 목록 초기화 (객체 상태 정리)
        existingBoard.getAttachedFileList().clear();
    }



    // 새로운 첨부파일이 있을 경우 처리
    @Transactional
    public void addNewFiles(Board existingBoard, MultipartFile [] files, Long boardId, HttpSession session) throws IOException{



        // 새로운 파일 업로드,저장
        for(MultipartFile file : files){
            if(!file.isEmpty()){
                // 새 파일을 업로드하고 저장
                try {
                    // 첨부파일 DTO 생성
                    AttachedFileDTO attachedFileDTO = new AttachedFileDTO();
                    // 기존 게시물로 설정
                    attachedFileDTO.setBoardId(boardId);

                    // 파일 업로드 처리 중
                    AttachedFile attachedFile = uploadFilesForUpdate(file,attachedFileDTO,session);

                    // 파일을 게시물에 추가
                    existingBoard.addAttachedFile(attachedFile);


                }
                catch (IOException e){
                    throw new IOException("파일 추가 중 오류 발생");
                }



            }
        }
    }



    // 첨부파일 수정
    @Transactional
    public void updateFiles (Long boardId, MultipartFile [] files, HttpSession session) throws IOException{

        // 게시물 찾기
        Board existingBoard = boardRepository.findById(boardId).orElse(null);


        if (existingBoard == null){
            throw new IllegalArgumentException("게시물을 찾을 수 없습니다");
        }

        // 새로운 첨부파일이 있을경우
        if (files != null && files.length > 0) {
            // 기존 파일이 있을 경우에만 삭제 (데이터베이스와 실제 서버 둘다 삭제)
            if (!existingBoard.getAttachedFileList().isEmpty()) {
                try {
                    deleteFile(existingBoard);
                }
                catch (Exception e){
                    throw new IOException("기존파일 삭제 중 오류 발생",e);
                }
            }

            // 새로운 파일 추가

            try {
                addNewFiles(existingBoard,files,boardId,session);
            }catch (IOException e){
                throw new IOException("새로운 파일 추가 중 오류 발생",e);
            }
        }
        
        // 게시물 저장
        boardRepository.save(existingBoard);
    }


    // 게시물에 존재하는 첨부파일 모두삭제
    public void deleteAllFiles (AttachedFile attachedFile){
        try {
            // 저장 경로
            String uploadDir = "C:\\project\\uploads\\";
            // 파일 객체 생성 (매개변수 = 경로)
            File savedFile = new File(uploadDir + attachedFile.getSavedName());

            // 서버에 파일 존재시
            if(savedFile.exists()){
                savedFile.delete();
            }
        }
        catch (Exception e){
            log.info(e.getMessage());
        }
    }

    
    // 메뉴 첨부파일 업로드
    @Transactional
    public AttachedFile uploadMenuFilesUpdate(MultipartFile file, AttachedFileDTO attachedFileDTO, HttpSession session){

        System.out.println("upload 실행");
        // 사용자 아이디 가져오기
        Member member = (Member) session.getAttribute("member");


        // 임시 디렉토리
        String tempDir = "C:\\project\\news\\tmp\\";
        // 최종 저장 디렉토리
        String uploadDir = "C:\\project\\news\\";


        // 파일이 비어 있지 않을 경우
        if(!file.isEmpty()) {

            String originalFileName = file.getOriginalFilename();


            // 파일 저장 파일명 설정 (중복 방지)
            String savedFileName = System.currentTimeMillis() + originalFileName;


            // 실제 임시 디렉토리에 저장될 경로
            File tempFile = new File(tempDir + savedFileName);


            // 임시 파일 저장
            try {
                file.transferTo(tempFile);
            }
            catch (IOException e){
                throw new RuntimeException("파일 업로드 오류 발생 ",e);
            }


            // 임시 파일을 최종 저장 디렉토리로 이동
            File finalFile = new File(uploadDir + savedFileName);
            if(tempFile.renameTo(finalFile)){


                // 파일 정보 DTO 업데이트
                attachedFileDTO.setUploadId(member.getUserId());
                attachedFileDTO.setPath2(savedFileName);
                attachedFileDTO.setSavedName(savedFileName);
                attachedFileDTO.setFileSize(String.valueOf(finalFile.length()));

                // boardId값 명시적으로 설정
                attachedFileDTO.setBoardId(null);

                // DTO -> Entity 변환 후 저장
                AttachedFile attachedFile = attachedFileDTO.toEntity();
                System.out.println("boardId 값" + attachedFile.getBoardId());
                attachedFile = attachedFileRepository.save(attachedFile);

                // 저장된 파일을 리스트에 추가
                return attachedFile;
            }else{
                throw new RuntimeException("파일을 최종 디렉토리로 이동하는데 실패했습니다");
            }
        }


        return null;
    }
    
    

    // 메뉴 첨부파일 삭제
    @Transactional
    public void deleteMenuFiles(MenuCategory existingMenuId) throws IOException{

        // 메뉴 아이디에 기존 첨부파일이 존재할 경우
        for(AttachedFile attachedFile : existingMenuId.getAttachedFileList()){
            try {
                // 데이터베이스에서 삭제
                attachedFileRepository.delete(attachedFile);

                // 파일 저장 경로
                String uploadDir= "C:\\project\\news\\";
                // 저장된 파일 -> 파일 객체 생성(경로)
                File savedFile = new File(uploadDir + attachedFile.getSavedName());
                
                // 저장된 파일 존재 시 서버에서 삭제
                if (savedFile.exists()){
                    savedFile.delete();
                }
            }
            catch (Exception e){
                throw new IOException("파일 삭제 중 오류 발생");
            }
        }
        // 메뉴 목록 첨부파일 초기화
        existingMenuId.getAttachedFileList().clear();
    }



    // 메뉴 첨부파일 추가
    @Transactional
    public void addMenuFiles(MenuCategory existingMenuId, MultipartFile [] files, Long menuId, HttpSession session) throws IOException{

        System.out.println("add 실행");
        for(MultipartFile file : files){
            if(!file.isEmpty()){
                try {
                    // DTO 생성
                    AttachedFileDTO attachedFileDTO = new AttachedFileDTO();

                    // 기존 메뉴 ID 설정
                    attachedFileDTO.setMenuId(menuId);

                    // 파일 업로드 처리
                    AttachedFile attachedFile = uploadMenuFilesUpdate(file,attachedFileDTO,session);

                    // 뉴스에 첨부파일 추가
                    existingMenuId.addAttachedFile(attachedFile);
                }
                catch (Exception e){
                    e.printStackTrace();
                    throw new IOException("메뉴 첨부파일 추가 오류 발생 ");
                }
            }
        }
    }




    // 메뉴 첨부파일 수정
    @Transactional
    public void updateNews(Long menuId, MultipartFile [] files, HttpSession session) throws IOException{


        MenuCategory existingMenuId = menuCategoryRepository.findById(menuId).orElse(null);

        if(existingMenuId == null){
            throw new IllegalArgumentException("메뉴를 찾을 수 없습니다");
        }


        if(files != null && files.length > 0 ){
            if(!existingMenuId.getAttachedFileList().isEmpty()){
                try {
                    deleteMenuFiles(existingMenuId);
                }
                catch (Exception e){
                    throw new IOException("메뉴 첨부파일 삭제 중 오류 ");
                }
            }
            try {
                addMenuFiles(existingMenuId, files, menuId, session);
            }
            catch (Exception e){
                throw new IllegalArgumentException("메뉴 첨부파일 추가 중 오류");
            }
        }
        // 메뉴 카테고리에 저장
        menuCategoryRepository.save(existingMenuId);
    }





    public List<AttachedFile> getFilesByMenuId(Long menuId){
        return attachedFileRepository.findByMenuId(menuId);
    }



    public Resource getNewsFile (String filename){
        try {

            // 파일을 저장한 디렉토리 경로
            String uploadDir= "C:\\project\\news\\";

            // 지정된 경로에 있는 파일 -> 객체를 생성
            File file = new File(uploadDir + filename);


            // 파일이 존재하지 않으면
            if(!file.exists()){
                return null;
            }

            // 파일이 존재하면 -> FileSystemResource 객체로 파일을 래핑하여 반환
            // FileSystemResource -> Resource 인터페이스 구현체 , 파일을 읽고 쓸수 있음
            return new FileSystemResource(file);
        }
        catch (Exception e){
            log.info(e.getMessage());
            return null;
        }
    }



}
