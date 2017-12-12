package com.netcracker.dao.manager.query;

import javafx.util.Pair;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

public class QueryDescriptor {
    private String innerQuery;
    private Pair<Integer, Integer> pagingDesc;
    private Map<Integer, Pair<String, Boolean>> sortingDesc;

    public String getInnerQuery() {
        return innerQuery;
    }

    public void setInnerQuery(String innerQuery) {
        this.innerQuery = innerQuery;
    }

    public Pair<Integer, Integer> getPagingDesc() {
        return pagingDesc;
    }

    public void addPagingDescriptor(Integer pageNumber, Integer pageCapacity) {
        pagingDesc = new Pair<>(pageNumber, pageCapacity);
    }

    public Map<Integer, Pair<String, Boolean>> getSortingDesc() {
        return sortingDesc;
    }

    public void addSortingDesc(Integer fieldArrtTypeId, String sortType, Boolean isDateValueSort) {
        if(sortingDesc == null){
            sortingDesc = new HashMap<>();
        }
        sortingDesc.put(fieldArrtTypeId, new Pair<>(sortType, isDateValueSort));
    }
}
