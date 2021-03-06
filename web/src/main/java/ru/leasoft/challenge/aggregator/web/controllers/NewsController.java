package ru.leasoft.challenge.aggregator.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.leasoft.challenge.aggregator.web.NewsRepositoryAccess;
import ru.leasoft.challenge.aggregator.web.dto.BadRequestException;
import ru.leasoft.challenge.aggregator.web.dto.NewsDto;
import ru.leasoft.challenge.aggregator.web.dto.SuggestionsDto;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@RestController
public class NewsController {

    private NewsRepositoryAccess service;

    private static final int PAGE_SIZE = 5;

    @Autowired
    public NewsController(NewsRepositoryAccess service) {
        this.service = service;
    }

    @RequestMapping(value = "/api/news", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Future<List<NewsDto>> getNewsAsync(@RequestParam int page) {
        if (page <= 0) throw new BadRequestException();
        int from = (page - 1) * PAGE_SIZE;

        CompletableFuture<List<NewsDto>> future = new CompletableFuture<>();
        service.loadNewsAsync(future, from, PAGE_SIZE);
        return future;
    }

    @RequestMapping(value = "/api/search", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Future<List<NewsDto>> getNewsAsync(@RequestParam String query, @RequestParam int page) {
        if (page <= 0) throw new BadRequestException();
        if (query == null || query.replace(" ", "").isEmpty()) throw new BadRequestException();
        int from = (page - 1) * PAGE_SIZE;

        CompletableFuture<List<NewsDto>> future = new CompletableFuture<>();
        service.searchInTitlePaginatedAsync(future, query, from, PAGE_SIZE);
        return future;
    }

    @RequestMapping(value = "/api/suggest", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Future<SuggestionsDto> suggestAsync(@RequestParam String propose) {
        CompletableFuture<SuggestionsDto> future = new CompletableFuture<>();

        if (propose == null || propose.length() <= 1 || propose.length() > 5) {
            SuggestionsDto dto = new SuggestionsDto();
            dto.setSuggestions(new ArrayList<>());
            future.complete(dto);
            return future;
        }

        service.suggest(future, propose);
        return future;
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(Exception e) {
        //nothing to do
    }

}
