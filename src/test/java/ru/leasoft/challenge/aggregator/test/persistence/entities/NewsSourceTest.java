package ru.leasoft.challenge.aggregator.test.persistence.entities;

import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.leasoft.challenge.aggregator.persistence.entities.NewsSource;
import ru.leasoft.challenge.aggregator.test.persistence.dao.TestEntityHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring/aggregator-test.xml")
public class NewsSourceTest {

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    @DirtiesContext
    public void testIsSameOnTransientAndPersistent() {
        NewsSource first = TestEntityHelper.makePersistedNewsSource(1, sessionFactory);
        NewsSource second = TestEntityHelper.makeTransientNewsSource(1);
        NewsSource third = TestEntityHelper.makeTransientNewsSource(2);

        Assert.assertTrue(first.isSameAs(second));
        Assert.assertTrue(second.isSameAs(first));
        Assert.assertFalse(first.isSameAs(third));
        Assert.assertFalse(third.isSameAs(first));
    }

}
