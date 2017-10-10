package ru.leasoft.challenge.aggregator.persistence.dao;

import ru.leasoft.challenge.aggregator.persistence.entities.NewsSource;

import java.util.List;

public interface NewsSourcesService {

    List<NewsSource> getAll();
    long create(NewsSource source);
    NewsSource load(String name, String url);

}
