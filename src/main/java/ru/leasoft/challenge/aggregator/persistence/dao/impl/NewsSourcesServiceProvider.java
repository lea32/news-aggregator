package ru.leasoft.challenge.aggregator.persistence.dao.impl;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.leasoft.challenge.aggregator.persistence.dao.NewsSourcesService;
import ru.leasoft.challenge.aggregator.persistence.entities.NewsSource;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class NewsSourcesServiceProvider implements NewsSourcesService {

    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;

    @Transactional(readOnly = true)
    public List<NewsSource> getAll() {
        Query<NewsSource> query = sessionFactory.getCurrentSession().createQuery("from NewsSource n", NewsSource.class);
        return query.list();
    }

    @Transactional
    public long create(NewsSource source) {
        return (long) sessionFactory.getCurrentSession().save(source);
    }
}
