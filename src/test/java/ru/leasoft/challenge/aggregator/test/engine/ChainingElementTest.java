package ru.leasoft.challenge.aggregator.test.engine;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;
import ru.leasoft.challenge.aggregator.engine.parsing.api.ChainingElement;

public class ChainingElementTest {

    @Test
    public void testUnEscaping() {
        Document doc = Jsoup.parse("Вчера было &lt;a href=\"http://adm-tver.ru/news/detail.php?id=864\" target=\"_blank\"&gt;опубликовано&lt;/a&gt;");
        ChainingElement e = new ChainingElement(doc);
        Assert.assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  Вчера было <a href=\"http://adm-tver.ru/news/detail.php?id=864\" target=\"_blank\">опубликовано</a>\n" +
                " </body>\n" +
                "</html>", e.getText());
    }

}
