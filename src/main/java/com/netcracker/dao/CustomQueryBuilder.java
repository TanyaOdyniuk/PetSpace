package com.netcracker.dao;

import javafx.util.Pair;

import java.util.Map;

public class CustomQueryBuilder {
    public static String build(boolean isPaging, String innerQuery, Pair<Integer, Integer> pagingDesc, Map<String, String> sortingDesc) {
        String result = innerQuery;
        StringBuilder stringBuilder = new StringBuilder();
        if (sortingDesc != null) {
            stringBuilder.append(innerQuery).append(" ORDER BY ");
            for (Map.Entry<String, String> item : sortingDesc.entrySet()) {
                stringBuilder.append(item.getKey())
                        .append(" ")
                        .append(item.getValue())
                        .append(",");
            }
            stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
            result = stringBuilder.toString();
        }
        if (isPaging) {
            stringBuilder = new StringBuilder("SELECT * FROM( SELECT a.*, rownum rr FROM( ")
                    .append(result).append(") a ")
                    .append("WHERE rownum < ").append((pagingDesc.getKey() * pagingDesc.getValue()) + 1)
                    .append(" ) WHERE rr >= ").append(((pagingDesc.getKey() - 1) * pagingDesc.getValue()) + 1);
            result = stringBuilder.toString();
        }
        return result;
    }
/*
    public static void main(String[] args) {
        Map<String, String> sortParam = new HashMap<>();
        sortParam.put("Object_id", "");
        sortParam.put("ObjectType_id", "DESC");
        System.out.println(CustomQueryBuilder.build(false, Query.SELECT_FROM_OBJECTS_BY_ID, null, sortParam));
        System.out.println(CustomQueryBuilder.build(true, Query.SELECT_FROM_OBJECTS_BY_ID, new Pair<>(2, 10), sortParam));
    }
*/
}
