package ru.leasoft.challenge.aggregator.test.persistence.entities;

import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.leasoft.challenge.aggregator.persistence.entities.News;
import ru.leasoft.challenge.aggregator.persistence.entities.NewsSource;
import ru.leasoft.challenge.aggregator.test.persistence.dao.TestEntityHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring/aggregator-test.xml")
public class NewsTest {

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    @DirtiesContext
    public void testIsSameOnTransientAndPersisted() {
        NewsSource src1 = TestEntityHelper.makePersistedNewsSource(1, sessionFactory);
        NewsSource src2 = TestEntityHelper.makePersistedNewsSource(2, sessionFactory);

        News first = TestEntityHelper.makeTransientNews(1);
        first.setNewsSource(src1);

        News second = TestEntityHelper.makePersistedNews(1, src1, sessionFactory);
        News third = TestEntityHelper.makePersistedNews(1, src2, sessionFactory);
        News fourth = TestEntityHelper.makeTransientNews(2);

        Assert.assertTrue(first.isSameAs(second));
        Assert.assertTrue(second.isSameAs(first));

        Assert.assertFalse(first.isSameAs(third));
        Assert.assertFalse(first.isSameAs(fourth));
    }

}
