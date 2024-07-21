package me.philocsera.springbootdeveloper.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.philocsera.springbootdeveloper.domain.Article;
import me.philocsera.springbootdeveloper.dto.AddArticleRequest;
import me.philocsera.springbootdeveloper.dto.UpdateArticleRequest;
import me.philocsera.springbootdeveloper.repository.BlogRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BlogService {
    private final BlogRepository blogRepository;

    public Article save(AddArticleRequest request){
        return blogRepository.save(request.toEntity());
    }

    public List<Article> findAll(){
        return blogRepository.findAll();
    }

    public Article findById(long id){
        return blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("NOT FOUND: " + id));
    }

    public void delete(long id){
        blogRepository.deleteById(id);
    }

    @Transactional
    public Article update(long id, UpdateArticleRequest request){
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("NOT FOUND" + id));

        article.update(request.getTitle(), request.getContent());

        return article;
    }
}
