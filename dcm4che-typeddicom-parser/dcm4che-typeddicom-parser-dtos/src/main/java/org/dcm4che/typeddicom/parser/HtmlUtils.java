package org.dcm4che.typeddicom.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class HtmlUtils {
    private HtmlUtils() {
    }

    public static String cleanUpHtml(String uncleanHtml) {
        Document jsoupDoc = Jsoup.parse(uncleanHtml);
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(true);
        jsoupDoc.select("br").after("\\n");
        jsoupDoc.select("p").before("\\n");
        jsoupDoc.outputSettings(outputSettings);
        Element body = jsoupDoc.body();
        sanitizeJDocHtml(body);
        String htmlString = body.html();
        htmlString = htmlString.replace("\\n", "\n");
        htmlString = htmlString.replace("\n<br>", "<br>\n");
        htmlString = htmlString.replaceAll("</p>(?!\\n)", "</p>\n");
        htmlString = htmlString.replaceAll("\\n+", "\n");
        htmlString = htmlString.trim();
        return htmlString;
    }

    private static void sanitizeJDocHtml(Element body) {
        for (Element element : body.select("dl > p")) {
            assert element.parent() != null; // should always have a parent
            element.parent().before(element);
        }

        for (Element element: body.select("p")) {
            element.html(element.html().trim());
        }

        for (Element element : body.select("p:empty")) {
            element.remove();
        }

        for (Element element : body.select("dd:empty, dd:matchesOwn((?is))")) {
            element.remove();
        }
    }
}
