package ru.leasoft.challenge.aggregator.persistence.dao;

import ru.leasoft.challenge.aggregator.persistence.entities.News;
import ru.leasoft.challenge.aggregator.persistence.entities.NewsSource;

import java.util.List;

public interface NewsService {

    List<News> getAll();
    boolean isNewsExist(String title, String content);
    long create(News news);
    List<News> getNewsForSource(NewsSource source);

}
