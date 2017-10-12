package ru.leasoft.challenge.aggregator.web.dto;

import java.util.List;

public class SuggestionsDto {

    private String query;
    private List<String> suggestions;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }
}
