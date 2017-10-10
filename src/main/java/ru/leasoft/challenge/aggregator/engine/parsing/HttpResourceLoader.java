package ru.leasoft.challenge.aggregator.engine.parsing;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class HttpResourceLoader {

    private WebClient webClient;

    private HttpClient httpClient;

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1";

    private interface ContentLoader {
        String load(String url) throws IOException;
    }

    private Map<String, ContentLoader> contentByTypeLoaders = new HashMap<>();

    public HttpResourceLoader() {
        webClient = new WebClient(BrowserVersion.FIREFOX_17);
        webClient.setJavaScriptTimeout(5000);

        httpClient = HttpClientBuilder.create().build();

        contentByTypeLoaders.put("application/rss+xml", this::loadXml);
        contentByTypeLoaders.put("text/xml", this::loadXml);
        contentByTypeLoaders.put("text/html", this::loadHtml);
    }

    public String get(String url) throws IOException {
        HttpHead headRequest = new HttpHead(url);
        headRequest.setHeader("User-Agent", USER_AGENT);

        HttpResponse response = httpClient.execute(headRequest);

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

    private String loadXml(String url) throws IOException {
        HttpGet getRequest = new HttpGet(url);
        getRequest.setHeader("User-Agent", USER_AGENT);

        String content = "";

        HttpResponse response = httpClient.execute(getRequest);
        if (response.getEntity() != null) {
            content = IOUtils.toString(response.getEntity().getContent(), Charset.forName("UTF-8"));
        }

        return content;
    }

    private String loadHtml(String url) {
        throw new ParsingException("Not realized yet");
    }

}
