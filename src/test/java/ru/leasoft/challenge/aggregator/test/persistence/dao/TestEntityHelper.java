package ru.leasoft.challenge.aggregator.test.persistence.dao;

import org.hibernate.SessionFactory;
import ru.leasoft.challenge.aggregator.persistence.entities.News;
import ru.leasoft.challenge.aggregator.persistence.entities.NewsSource;

public class TestEntityHelper {

    public static News makeTransientNews(int seed) {
        News news = new News();
        news.setTitle("Test" + seed);
        news.setContent("Test-content" + seed);
        return news;
    }

    public static NewsSource makeTransientNewsSource(int seed) {
        NewsSource src = new NewsSource();
        src.setName("TestSrc" + seed);
        src.setLocation("testLoc" + seed);
        return src;
    }

    public static NewsSource makePersistedNewsSource(int seed, SessionFactory sessionFactory) {
        NewsSource src = makeTransientNewsSource(seed);

        return TransactionExecutor.with(sessionFactory).execute(session -> {
            long id = (long) session.save(src);
            return session.get(NewsSource.class, id);
        });
    }

    public static News makePersistedNews(int seed, NewsSource persistedNewsSource, SessionFactory sessionFactory) {
        News news = makeTransientNews(seed);
        news.setNewsSource(persistedNewsSource);

        return TransactionExecutor.with(sessionFactory).execute(session -> {
           long id = (long) session.save(news);
           return session.get(News.class, id);
        });
    }

}
