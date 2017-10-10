package ru.leasoft.challenge.aggregator.engine.helpers;

import ru.leasoft.challenge.aggregator.persistence.entities.NewsSource;

public interface NewsRepository {

    void appendNews(String title, String content, NewsSource source);
    NewsSource loadNewsSourceOrCreateIfNotExist(String name, String url);

}
