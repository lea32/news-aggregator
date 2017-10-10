package ru.leasoft.challenge.aggregator.engine.parsing.api;

import ru.leasoft.challenge.aggregator.container.ContextAccessPoint;
import ru.leasoft.challenge.aggregator.engine.core.ParsingTarget;
import ru.leasoft.challenge.aggregator.engine.helpers.NewsRepository;
import ru.leasoft.challenge.aggregator.engine.parsing.HttpResourceLoader;
import ru.leasoft.challenge.aggregator.persistence.entities.NewsSource;

import java.io.IOException;

public class ParsingApi {

    private NewsRepository repository;
    private HttpResourceLoader loader;

    private NewsSource newsSource;

    public ParsingApi(ParsingTarget target) {
        repository = ContextAccessPoint.getBeanFromContext("newsRepository", NewsRepository.class);
        newsSource = repository.loadNewsSourceOrCreateIfNotExist(target.getName(), target.getUrl());

        loader = new HttpResourceLoader();
    }

    public void echo(String string) {
        System.out.println(string);
    }

    public void appendNews(String title, String content) {
        repository.appendNews(title, content, newsSource);
    }

    public XmlContent loadResource(String resourceUrl) throws IOException {
        return new XmlContent(loader.get(resourceUrl));
    }

}
