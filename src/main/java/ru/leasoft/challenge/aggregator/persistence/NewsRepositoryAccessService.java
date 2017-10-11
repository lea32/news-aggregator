package ru.leasoft.challenge.aggregator.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import ru.leasoft.challenge.aggregator.persistence.dao.NewsService;
import ru.leasoft.challenge.aggregator.persistence.entities.News;
import ru.leasoft.challenge.aggregator.web.NewsRepositoryAccess;
import ru.leasoft.challenge.aggregator.web.dto.NewsDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsRepositoryAccessService implements NewsRepositoryAccess {

    private NewsService newsService;

    @Autowired
    public NewsRepositoryAccessService(NewsService news) {
        this.newsService = news;
    }

    @Override
    public List<NewsDto> loadNews() {
        return newsService.getAll()
                .stream()
                .map(News::packToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Async
    public void loadNewsAsync(DeferredResult<List<NewsDto>> result) {
        if (result.isSetOrExpired()) return;

        result.setResult(loadNews());
    }
}
