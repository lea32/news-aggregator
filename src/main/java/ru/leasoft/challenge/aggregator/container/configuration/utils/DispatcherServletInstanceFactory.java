package ru.leasoft.challenge.aggregator.container.configuration.utils;

import io.undertow.servlet.api.InstanceFactory;
import io.undertow.servlet.api.InstanceHandle;
import org.springframework.web.servlet.DispatcherServlet;

public class DispatcherServletInstanceFactory implements InstanceFactory<DispatcherServlet> {

    public InstanceHandle<DispatcherServlet> createInstance() throws InstantiationException {
        return new DispatcherServletInstanceHandle();
    }

    private class DispatcherServletInstanceHandle implements InstanceHandle<DispatcherServlet> {

        @Override
        public DispatcherServlet getInstance() {
            return new DispatcherServlet();
        }

        @Override
        public void release() {

        }

    }

}
