package ru.leasoft.challenge.aggregator.persistence.dao.impl;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.leasoft.challenge.aggregator.persistence.dao.NewsService;
import ru.leasoft.challenge.aggregator.persistence.entities.News;
import ru.leasoft.challenge.aggregator.persistence.entities.NewsSource;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class NewsServiceProvider implements NewsService {

    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;

    @Transactional(readOnly = true)
    public List<News> getAll() {
        Query<News> query = sessionFactory.getCurrentSession().createQuery("from News n", News.class);
        return query.list();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isNewsExist(String title, String content) {
        Query<Long> query = sessionFactory.getCurrentSession().createQuery(
                "select count(n) from News n where n.title = :title and n.content = :content",
                Long.class
        );
        query.setParameter("title", title);
        query.setParameter("content", content);
        return query.uniqueResult() > 0;
    }

    @Override
    @Transactional
    public long create(News news) {
        return (long) sessionFactory.getCurrentSession().save(news);
    }

    @Override
    @Transactional(readOnly = true)
    public List<News> getNewsForSource(NewsSource source) {
        Query<News> query = sessionFactory.getCurrentSession().createQuery(
                "from News n where n.newsSource = :source",
                News.class
        );
        query.setParameter("source", source);
        return query.list();
    }
}
