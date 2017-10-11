package ru.leasoft.challenge.aggregator.persistence.dao.impl;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
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
    public boolean isNewsExist(String title, String content, NewsSource fromSource) {
        Query<Long> query = sessionFactory.getCurrentSession().createQuery(
                "select count(n) from News n where n.newsSource = :fromSource and n.title = :title and n.content = :content",
                Long.class
        );
        query.setParameter("fromSource", fromSource);
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

    @Override
    @Transactional(readOnly = true)
    public List<News> getPaginated(int from, int count) {
        Query<News> query = sessionFactory.getCurrentSession().createQuery(
                "from News n order by n.appendTimestamp desc",
                News.class
        );
        query.setFirstResult(from);
        query.setMaxResults(count);

        return query.list();
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<News> searchInTitlePaginated(String queryString, int from, int count) {
        FullTextSession ftSession = Search.getFullTextSession(sessionFactory.getCurrentSession());
        QueryBuilder qb = ftSession.getSearchFactory().buildQueryBuilder().forEntity(News.class).get();
        org.apache.lucene.search.Query luceneQuery =
                qb.keyword()
                  .onField("title")
                  .matching(queryString)
                  .createQuery();
        FullTextQuery query = ftSession.createFullTextQuery(luceneQuery, News.class)
                .setFirstResult(from)
                .setMaxResults(count);
        return query.list();
    }
}
