package ru.leasoft.challenge.aggregator.web;

import org.springframework.web.context.request.async.DeferredResult;
import ru.leasoft.challenge.aggregator.web.dto.NewsDto;

import java.util.List;

public interface NewsRepositoryAccess {

    List<NewsDto> loadNews();

    void loadNewsAsync(DeferredResult<List<NewsDto>> result);

}
