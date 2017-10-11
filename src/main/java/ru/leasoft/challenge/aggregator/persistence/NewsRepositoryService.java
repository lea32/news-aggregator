package ru.leasoft.challenge.aggregator.persistence;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.leasoft.challenge.aggregator.engine.helpers.NewsRepository;
import ru.leasoft.challenge.aggregator.persistence.aspects.PersistenceMethod;
import ru.leasoft.challenge.aggregator.persistence.dao.NewsService;
import ru.leasoft.challenge.aggregator.persistence.dao.NewsSourcesService;
import ru.leasoft.challenge.aggregator.persistence.entities.News;
import ru.leasoft.challenge.aggregator.persistence.entities.NewsSource;

@Service("newsRepository")
public class NewsRepositoryService implements NewsRepository {

    private NewsService newsService;
    private NewsSourcesService newsSourcesService;

    @Autowired
    public NewsRepositoryService(NewsService newsService, NewsSourcesService newsSourcesService) {
        this.newsService = newsService;
        this.newsSourcesService = newsSourcesService;
    }

    @Override
    @PersistenceMethod
    public void appendNews(String title, String content, NewsSource source) {
        if (StringUtils.isBlank(content)) return;
        if (StringUtils.isBlank(title)) {
            title = StringUtils.abbreviate(content, 50);
        }

        title = StringUtils.abbreviate(title, News.MAX_TITLE_LENGTH).replaceAll("[^\\u0000-\\uFFFF]", ""); //just believe, we must do it for the great mySql justice ;))
        content = StringUtils.abbreviate(content, News.MAX_CONTENT_LENGTH).replaceAll("[^\\u0000-\\uFFFF]", "");

        boolean newsExist = newsService.isNewsExist(title, content, source);
        if (!newsExist) {
            News news = new News();
            news.setTitle(title);
            news.setContent(content);
            news.setNewsSource(source);
            newsService.create(news);
        }
    }

    @Override
    @PersistenceMethod
    public NewsSource loadNewsSourceOrCreateIfNotExist(String name, String url) {
        NewsSource source = newsSourcesService.load(name, url);
        if (source == null) {
            source = new NewsSource();
            source.setLocation(url);
            source.setName(name);
            newsSourcesService.create(source);
            if (source.getId() == 0) throw new RuntimeException("Fails to save news source"); //unreachable in normal conditions
        }
        return source;
    }
}
