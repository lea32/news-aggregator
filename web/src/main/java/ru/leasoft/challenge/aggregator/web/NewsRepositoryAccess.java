package ru.leasoft.challenge.aggregator.web;

import ru.leasoft.challenge.aggregator.web.dto.NewsDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface NewsRepositoryAccess {

    List<NewsDto> loadNews(int from, int count);

    void loadNewsAsync(CompletableFuture<List<NewsDto>> result, int from, int count);

    void searchInTitlePaginatedAsync(CompletableFuture<List<NewsDto>> result, String queryString, int from, int count);

}
