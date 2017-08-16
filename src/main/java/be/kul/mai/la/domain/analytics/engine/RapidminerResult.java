package be.kul.mai.la.domain.analytics.engine;

import com.rapidminer.operator.text.Document;

public class RapidminerResult {

    private Document document;

    public RapidminerResult(Document document) {
        this.document = document;
    }

    public String toJSON(){
        return document.getTokenText();
    }
}
