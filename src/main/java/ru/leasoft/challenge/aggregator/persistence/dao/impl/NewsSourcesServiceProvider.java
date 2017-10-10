package ru.leasoft.challenge.aggregator.persistence.dao.impl;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.leasoft.challenge.aggregator.persistence.dao.NewsService;
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
    public synchronized long create(NewsSource source) {
        if (load(source.getName(), source.getLocation()) == null) {
            return (long) sessionFactory.getCurrentSession().save(source);
        } else return 0;
    }

    @Override
    @Transactional(readOnly = true)
    public NewsSource load(String name, String url) {
        Query<NewsSource> query = sessionFactory.getCurrentSession().createQuery(
                "from NewsSource n where n.name = :name and n.location = :url", NewsSource.class);
        query.setParameter("name", name);
        query.setParameter("url", url);
        return query.uniqueResult();
    }
}
