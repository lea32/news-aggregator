package ru.leasoft.challenge.aggregator.engine.parsing;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpResourceLoader {

    private HttpClient httpClient;

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1";

    private static final int LOADING_TIMEOUT = 5000;

    private interface ContentLoader {
        Document load(String url) throws IOException;
    }

    private Map<String, ContentLoader> contentByTypeLoaders = new HashMap<>();

    public HttpResourceLoader() {
        httpClient = HttpClientBuilder.create().build();

        contentByTypeLoaders.put("application/rss+xml", this::loadXml);
        contentByTypeLoaders.put("application/xml", this::loadXml);
        contentByTypeLoaders.put("text/xml", this::loadXml);
        contentByTypeLoaders.put("text/html", this::loadHtml);
        contentByTypeLoaders.put("application/xhtml+xml", this::loadHtml);
    }

    public Document get(String url) throws IOException {

        HttpResponse response = headRequest(url);

        if (response.getStatusLine().getStatusCode() != 200) {
            throw new ParsingException("Invalid status code " + response.getStatusLine().getStatusCode());
        }

        Header typeHeader = response.getFirstHeader("Content-Type");
        if (typeHeader == null) {
            throw new ParsingException("Missing content type header in response. Unsupported resource type.");
        }

        String contentType = typeHeader.getValue();

        for (String pattern: contentByTypeLoaders.keySet()) {
            if (contentType.contains(pattern)) {

                return contentByTypeLoaders.get(pattern).load(url);
            }
        }

        throw new ParsingException("Unsupported content type " + contentType);
    }

    private HttpResponse headRequest(String url) {
        try {
            return new RequestHeadCommand(url).execute();
        } catch (Throwable t) {
            if (t.getCause() != null) {
                throw new ParsingException(t.getCause().getMessage());
            } else {
                throw new ParsingException("Connection timeout: " + url);
            }
        }
    }

    private Document loadXml(String url) throws IOException {
        return Jsoup.connect(url).timeout(LOADING_TIMEOUT).parser(Parser.xmlParser()).ignoreContentType(true).get();
    }

    private Document loadHtml(String url) throws IOException {
        return Jsoup.connect(url).timeout(LOADING_TIMEOUT).parser(Parser.xmlParser()).get();
    }

    private class RequestHeadCommand extends HystrixCommand<HttpResponse> {

        private String url;

        RequestHeadCommand(String url) {
            super(Setter.withGroupKey(
                    HystrixCommandGroupKey.Factory.asKey("RequestHeadCommand"))
                    .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                            .withExecutionTimeoutInMilliseconds(LOADING_TIMEOUT))
            );
            this.url = url;
        }

        @Override
        protected HttpResponse run() throws Exception {
            HttpHead headRequest = new HttpHead(url);
            headRequest.setHeader("User-Agent", USER_AGENT);
            return httpClient.execute(headRequest);
        }
    }

}
