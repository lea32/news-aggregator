package ru.leasoft.challenge.aggregator.persistence.dao;

import ru.leasoft.challenge.aggregator.persistence.entities.News;
import ru.leasoft.challenge.aggregator.persistence.entities.NewsSource;

import java.util.List;

public interface NewsService {

    List<News> getAll();
    List<News> getPaginated(int from, int count);
    boolean isNewsExist(String title, String content, NewsSource fromSource);
    long create(News news);
    List<News> getNewsForSource(NewsSource source);
    List<News> searchInTitlePaginated(String queryString, int from, int count);

}
