package ru.leasoft.challenge.aggregator.test.persistence.dao;

import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.leasoft.challenge.aggregator.persistence.dao.NewsSourcesService;
import ru.leasoft.challenge.aggregator.persistence.entities.NewsSource;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring/aggregator-test.xml")
public class NewsSourcesServiceTest {

    @Autowired
    private NewsSourcesService newsSourcesService;
    @Autowired
    private SessionFactory sessionFactory;

    @Test
    @DirtiesContext
    public void creationTest() {
        NewsSource newsSource = TestEntityHelper.makeTransientNewsSource(1);

        long id = newsSourcesService.create(newsSource);
        Assert.assertTrue(id != 0);

        NewsSource restored = TransactionExecutor.with(sessionFactory)
                .execute(session -> session.get(NewsSource.class, id));
        Assert.assertNotNull(restored);
        Assert.assertTrue(newsSource.isSameAs(restored));
    }

    @Test
    @DirtiesContext
    public void getAllTest() {
        for (int i = 1; i <= 10; i++) {
            NewsSource newsSource = TestEntityHelper.makeTransientNewsSource(i);
            newsSourcesService.create(newsSource);
        }

        List<NewsSource> sources = newsSourcesService.getAll();
        Assert.assertNotNull(sources);
        Assert.assertEquals(10, sources.size());
    }

}
