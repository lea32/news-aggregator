package ru.leasoft.challenge.aggregator.container.configuration.utils;

import io.undertow.servlet.api.InstanceFactory;
import io.undertow.servlet.api.InstanceHandle;
import org.springframework.web.context.ContextLoaderListener;

public class ContextLoaderListenerInstanceFactory implements InstanceFactory<ContextLoaderListener> {

    @Override
    public InstanceHandle<ContextLoaderListener> createInstance() throws InstantiationException {
        return new InstanceHandle<ContextLoaderListener>() {
            @Override
            public ContextLoaderListener getInstance() {
                return new ContextLoaderListener();
            }

            @Override
            public void release() {}
        };
    }
}
