package com.netcracker.dao.manager.query;

import javafx.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class QueryBuilder {
    private String buildWithSortDesc(Map<Integer, Pair<String, Boolean>> sortingDescriptor, String innerQuery){
        String result = innerQuery;
        StringBuilder stringBuilder = new StringBuilder();
        if (sortingDescriptor != null) {
            int counter = 0;
            stringBuilder.append("select a.* from ( ")
                    .append(result).append(" ) a");
            for (Integer fieldAttrTypeId : sortingDescriptor.keySet()) {
                stringBuilder.append(", attributes at").append(counter);
                counter++;
            }
            counter = 0;
            stringBuilder.append(" WHERE ");
            for (Integer fieldAttrTypeId : sortingDescriptor.keySet()) {
                stringBuilder.append("at").append(counter).append(".ATTRTYPE_ID = ").append(fieldAttrTypeId)
                        .append(" and at").append(counter).append(".OBJECT_ID = a.OBJECT_ID").append(" and ");
                counter++;
            }
            stringBuilder.delete(stringBuilder.lastIndexOf(" ") - 4, stringBuilder.lastIndexOf(" "));
            stringBuilder.append("ORDER BY ");
            counter = 0;
            for (Pair<String, Boolean> fieldDesc : sortingDescriptor.values()) {
                stringBuilder.append("at").append(counter);
                if (fieldDesc.getValue()) {
                    stringBuilder.append(".DATE_VALUE ");
                } else {
                    stringBuilder.append(".VALUE ");
                }
                stringBuilder.append(fieldDesc.getKey()).append(",");
                counter++;
            }
            stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
            result = stringBuilder.toString();
        }
        return result;
    }

    private String buildWithPagingDesc(Pair<Integer, Integer> pagingDesc, String result){
        if(pagingDesc != null){
            StringBuilder stringBuilder = new StringBuilder("SELECT * FROM( SELECT a.*, rownum rr FROM( ")
                    .append(result).append(") a ")
                    .append("WHERE rownum < ").append((pagingDesc.getKey() * pagingDesc.getValue()) + 1)
                    .append(" ) WHERE rr >= ").append(((pagingDesc.getKey() - 1) * pagingDesc.getValue()) + 1);
            result = stringBuilder.toString();
        }
        return result;
    }

    public String build(QueryDescriptor queryDescriptor){
        String result = buildWithSortDesc(queryDescriptor.getSortingDesc(), queryDescriptor.getInnerQuery());
        return buildWithPagingDesc(queryDescriptor.getPagingDesc(), result);
    }

    public static String buildCountQuery(String innerQuery) {
        return "select max(ROWNUM) as c " +
                "from (" + innerQuery + ")";
    }

}
