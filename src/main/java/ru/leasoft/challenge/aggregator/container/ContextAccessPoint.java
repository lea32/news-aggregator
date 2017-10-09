package ru.leasoft.challenge.aggregator.container;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ContextAccessPoint implements ApplicationContextAware {

    private static ApplicationContext context;

    public static <E> E getBeanFromContext(String beanId, Class<E> clazz) {
        if (context == null) throw new RuntimeException("No web application context present");

        return context.getBean(beanId, clazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ContextAccessPoint.context = applicationContext;
    }
}
