package ru.leasoft.challenge.aggregator.engine.parsing.api;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.leasoft.challenge.aggregator.engine.parsing.ParsingException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChainingElement {

    private Element documentRoot;

    public ChainingElement() {
        documentRoot = null;
    }

    public ChainingElement(Document documentRoot) {
        this.documentRoot = documentRoot;
    }

    public ChainingElement(Element element) {
        this.documentRoot = element;
    }

    public ChainingElement findFirst(String selector)  {
        if (documentRoot == null) return new ChainingElement();

        Elements elements = select(selector);

        if (elements.size() > 0) {
            return new ChainingElement(elements.get(0));
        } else return new ChainingElement();
    }

    public List<ChainingElement> findAll(String selector) {
        if (documentRoot == null) return new ArrayList<>();

        Elements elements = select(selector);
        return elements.stream().map(ChainingElement::new).collect(Collectors.toList());
    }

    private Elements select(String selector) {
        try {
            return documentRoot.select(selector);
        } catch (IllegalArgumentException iaException) {
            throw new ParsingException("Invalid selector argument: " + selector);
        }
    }

    public String attr(String attr) {
        if (documentRoot == null) return "";

        return documentRoot.attr(attr);
    }

    public String getText() {
        return documentRoot != null ? StringEscapeUtils.unescapeHtml(documentRoot.html()) : "";
    }

    public String getPlain() {
        return documentRoot != null ? StringEscapeUtils.unescapeHtml(documentRoot.text()) : "";
    }

}
