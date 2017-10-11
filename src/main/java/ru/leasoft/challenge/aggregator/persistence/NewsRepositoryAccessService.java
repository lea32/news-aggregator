package ru.leasoft.challenge.aggregator.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.leasoft.challenge.aggregator.persistence.dao.NewsService;
import ru.leasoft.challenge.aggregator.persistence.entities.News;
import ru.leasoft.challenge.aggregator.web.NewsRepositoryAccess;
import ru.leasoft.challenge.aggregator.web.dto.NewsDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class NewsRepositoryAccessService implements NewsRepositoryAccess {

    private NewsService newsService;

    @Autowired
    public NewsRepositoryAccessService(NewsService news) {
        this.newsService = news;
    }

    @Override
    public List<NewsDto> loadNews(int from, int count) {

        return newsService.getPaginated(from, count)
                .stream()
                .map(News::packToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Async("webRequestsExecutor")
    public void loadNewsAsync(CompletableFuture<List<NewsDto>> result, int from, int count) {
        if (result.isDone()) return;

        result.complete(loadNews(from, count));
    }

    @Override
    @Async("webRequestsExecutor")
    public void searchInTitlePaginatedAsync(CompletableFuture<List<NewsDto>> result, String queryString, int from, int count) {
        if (result.isDone()) return;

        result.complete(newsService.searchInTitlePaginated(queryString, from, count)
                .stream()
                .map(News::packToDto)
                .collect(Collectors.toList()));
    }
}
