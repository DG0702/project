package com.project.project.api;

import com.project.project.dto.ArticleForm;
import com.project.project.entity.Article;
import com.project.project.repository.ArticleRepository;
import com.project.project.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class RestApiController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/api/articles")
    public List<Article> index() {
        return articleService.index();
    }

    @GetMapping("/api/articles/{id}")
    public ResponseEntity<Article> select (@PathVariable Long id){
        Article select =  articleService.select(id);
        if(select == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(select);
    }

    @PostMapping("/api/articles")
    public ResponseEntity<Article> create (@RequestBody ArticleForm dto){
        Article create = articleService.create(dto);
        if(create == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(create);
    }

    @PatchMapping("/api/articles/{id}")
    public ResponseEntity<Article> update (@PathVariable Long id, @RequestBody ArticleForm dto){
        Article update = articleService.update(id,dto);
        if(update == null || id != update.getId()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
         return ResponseEntity.status(HttpStatus.OK).body(update);
    }

    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Article> delete (@PathVariable Long id){
        Article delete =  articleService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("api/transaction-test")
    public ResponseEntity<List<Article>> transactionTest(@RequestBody List<ArticleForm> dtos){
        List<Article>  createdList = articleService.createArticles(dtos);
        return (createdList != null)?
                ResponseEntity.status(HttpStatus.OK).body(createdList):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }















//    @Autowired
//    private ArticleRepository articleRepository;
//
//    @GetMapping("/api/articles")
//    public List<Article> index () {
//        return articleRepository.findAll();
//    }
//
//    @GetMapping("/api/articles/{id}")
//    public Article index (@PathVariable Long id) {
//        return articleRepository.findById(id).orElse(null);
//    }
//
//    @PostMapping("/api/articles")
//    public Article create (@RequestBody ArticleForm dto) {
//        Article article = dto.toEntity();
//        return articleRepository.save(article);
//    }
//
//    @ResponseBody
//    @GetMapping("/goHome")
//    public ResponseEntity<Void> home() {
//        log.info("홈 출력");
//        return new ResponseEntity<Void>(HttpStatus.OK);
//    }
//
//    @PatchMapping("/api/articles/{id}")
//    public ResponseEntity<Article> update(@PathVariable Long id , @RequestBody ArticleForm dto){
//        Article article = dto.toEntity();
//        Article target = articleRepository.findById(article.getId()).orElse(null);
//        if(target == null || id != article.getId()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        }
//        target.patch(article);
//        Article updated = articleRepository.save(article);
//        return ResponseEntity.status(HttpStatus.OK).body(updated);
//    }
//
//    @DeleteMapping("/api/articles/{id}")
//    public ResponseEntity<Article> delete (@PathVariable Long id) {
//        Article article = articleRepository.findById(id).orElse(null);
//        if(article == null || id != article.getId()){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        }
//        articleRepository.delete(article);
//        return ResponseEntity.status(HttpStatus.OK).build();
//    }
}
