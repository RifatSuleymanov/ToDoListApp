package ru.suleymanov.entity.dto;

public class QueryParameters {
    private int id;
    private String filter;

    public QueryParameters() {
    }

    public QueryParameters(int id, String filter) {
        this.id = id;
        this.filter = filter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}
