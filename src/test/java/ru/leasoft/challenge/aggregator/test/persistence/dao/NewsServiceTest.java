package ru.leasoft.challenge.aggregator.test.persistence.dao;

import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.leasoft.challenge.aggregator.persistence.dao.NewsService;
import ru.leasoft.challenge.aggregator.persistence.entities.News;
import ru.leasoft.challenge.aggregator.persistence.entities.NewsSource;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring/aggregator-test.xml")
public class NewsServiceTest {

    @Autowired
    private NewsService newsService;
    @Autowired
    private SessionFactory sessionFactory;

    @Test
    @DirtiesContext
    public void testCreation() {
        News news = TestEntityHelper.makeTransientNews(1);
        NewsSource source = TestEntityHelper.makePersistedNewsSource(1, sessionFactory);
        news.setNewsSource(source);

        long id = newsService.create(news);
        Assert.assertTrue(id != 0);

        News restored = TransactionExecutor.with(sessionFactory).execute(session -> session.get(News.class, id));

        Assert.assertTrue(news.isSameAs(restored));
    }

    @Test
    @DirtiesContext
    public void testGetAll() {
        NewsSource src = TestEntityHelper.makePersistedNewsSource(1, sessionFactory);
        for (int i = 1; i <= 10; i++) {
            News news = TestEntityHelper.makeTransientNews(i);
            news.setNewsSource(src);
            newsService.create(news);
        }

        List<News> restored = newsService.getAll();
        Assert.assertNotNull(restored);
        Assert.assertEquals(10, restored.size());
    }

    @Test
    @DirtiesContext
    public void testGetForSource() {
        NewsSource src1 = TestEntityHelper.makePersistedNewsSource(1, sessionFactory);
        News news1 = TestEntityHelper.makePersistedNews(1, src1, sessionFactory);
        NewsSource src2 = TestEntityHelper.makePersistedNewsSource(2, sessionFactory);
        News news2 = TestEntityHelper.makePersistedNews(2, src2, sessionFactory);

        List<News> newsForSrc1 = newsService.getNewsForSource(src1);
        Assert.assertTrue(newsForSrc1.size() == 1);
        newsForSrc1.get(0).isSameAs(news1);

        List<News> newsForSrc2 = newsService.getNewsForSource(src2);
        Assert.assertTrue(newsForSrc2.size() == 1);
        newsForSrc2.get(0).isSameAs(news2);
    }

    @Test
    @DirtiesContext
    public void testNewsExist() {
        NewsSource src1 = TestEntityHelper.makePersistedNewsSource(1, sessionFactory);
        News news1 = TestEntityHelper.makePersistedNews(1, src1, sessionFactory);
        News news2 = TestEntityHelper.makeTransientNews(2);

        Assert.assertTrue(newsService.isNewsExist(news1.getTitle(), news1.getContent(), src1));
        Assert.assertFalse(newsService.isNewsExist(news2.getTitle(), news2.getContent(), src1));
    }

}
