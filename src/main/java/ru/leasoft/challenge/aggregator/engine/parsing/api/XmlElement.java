package ru.leasoft.challenge.aggregator.engine.parsing.api;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.leasoft.challenge.aggregator.engine.parsing.ParsingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class XmlElement {

    private Document document;
    private XPath xPath;

    private DocumentBuilder builder;

    public XmlElement(String xmlContent) throws IOException {
        try {

            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

        } catch (ParserConfigurationException pex) {
            throw new ParsingException("Xml parser exception: " + pex.getMessage());
        }

        if (!StringUtils.isBlank(xmlContent)) {
            this.document = xmlDocument(xmlContent);
        } else {
            this.document = builder.newDocument();
        }

        XPathFactory factory = XPathFactory.newInstance();
        xPath = factory.newXPath();
    }

    private Document xmlDocument(String source) throws IOException {
        try {
            return builder.parse(new InputSource(new StringReader(source)));

        } catch (SAXException saxEx) {
            throw new ParsingException("Invalid xml document: " + saxEx.getMessage());
        }
    }

    public XmlElement findFirst(String xPathPattern) throws IOException {
        NodeList nodeList = executePattern(xPathPattern);

        if (nodeList.getLength() > 0) {
            return new XmlElement(text(nodeList.item(0)));
        } else return new XmlElement("");
    }

    public List<XmlElement> findAll(String xPathPattern) throws IOException {
        NodeList nodeList = executePattern(xPathPattern);
        return wrap(nodeList);
    }

    private NodeList executePattern(String xPathPattern) {
        try {

            return (NodeList) xPath.evaluate(xPathPattern, document, XPathConstants.NODESET);

        } catch (XPathExpressionException e) {
            throw new ParsingException("Wrong xPath expression: " + e.getMessage());
        }
    }

    public String getText() {

        return (document.getFirstChild() != null) ?
                StringUtils.stripToEmpty(document.getFirstChild().getTextContent()) :
                "";
    }

    private List<XmlElement> wrap(NodeList nodeList) throws IOException {
        List<XmlElement> result = new ArrayList<>(nodeList.getLength());
        for (int i = 0; i < nodeList.getLength(); i++) {
            result.add(new XmlElement(text(nodeList.item(i))));
        }
        return result;
    }

    private String text(Document doc) {
        DOMImplementationLS domImplementation = (DOMImplementationLS) doc.getImplementation();
        LSSerializer lsSerializer = domImplementation.createLSSerializer();
        return lsSerializer.writeToString(doc);
    }

    private String text(Node node) {
        Document doc = builder.newDocument();
        Node importedNode = doc.importNode(node, true);
        doc.appendChild(importedNode);
        return text(doc);
    }

}
