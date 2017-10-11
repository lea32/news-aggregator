package ru.leasoft.challenge.aggregator.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.leasoft.challenge.aggregator.web.NewsRepositoryAccess;
import ru.leasoft.challenge.aggregator.web.dto.NewsDto;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@RestController
public class NewsController {

    private NewsRepositoryAccess service;

    @Autowired
    public NewsController(NewsRepositoryAccess service) {
        this.service = service;
    }

    @RequestMapping(value = "/news", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<NewsDto> getNews() {
        return service.loadNews();
    }

    @RequestMapping(value = "/async", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Future<String> getNewsAs() {
        CompletableFuture<String> future = new CompletableFuture<>();
        future.complete("test string");
        System.out.println("ASYNC");
        return future;
    }

}
