package ru.leasoft.challenge.aggregator.engine.parsing.api;

import org.dom4j.QName;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import ru.leasoft.challenge.aggregator.engine.parsing.ParsingException;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class XmlContent {

    private Document document;
    private XPath xPath;

    public XmlContent(Document document) {
        this.document = document;

        XPathFactory factory = XPathFactory.newInstance();
        xPath = factory.newXPath();
    }

    public String findFirst(String xPathPattern) {
        try {
            NodeList nodeList = (NodeList) xPath.evaluate(xPathPattern, document, XPathConstants.NODESET);
            return nodeList.item(0).getNodeName() + " == " + nodeList.item(0).getTextContent();

        } catch (XPathExpressionException e) {
            throw new ParsingException("Wrong xPath expression: " + e.getMessage());
        }
    }

}
