package me.philocsera.springbootdeveloper.dto;

import lombok.Getter;
import me.philocsera.springbootdeveloper.domain.Article;

@Getter
public class ArticleResponse {
    private final String title;
    private final String content;

    public ArticleResponse(Article article){
        title = article.getTitle();
        content = article.getContent();
    }
}
