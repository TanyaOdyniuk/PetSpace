package com.netcracker.dao.manager;

import com.netcracker.dao.Entity;
import com.netcracker.dao.manager.query.Query;
import javafx.util.Pair;
//import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.*;
import java.sql.Date;
import java.util.*;

//@Repository
public class EntityManager {

    private SimpleJdbcCall jdbcCall;
    private JdbcTemplate jdbcTemplate;

    //@Autowired
    public EntityManager(DataSource dataSource) {
        try {
            Connection c =  dataSource.getConnection("ODESSA_17", "ODESSA_17");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);
        this.jdbcCall = new SimpleJdbcCall(jdbcTemplate);
    }

    @Transactional
    public Entity create(Entity entity) {
        //KeyHolder keyHolder = new GeneratedKeyHolder();
        //entity.setObjectId(keyHolder.getKey().intValue());
        Map<String, Object> result = executeObjectJdbcCall(entity, jdbcCall, 0, null);
        entity.setObjectId((Integer) result.get("p_OBJECT_ID"));
        for (Map.Entry entry : entity.getReferences().entrySet()) {
            if (!(entry.getValue().toString().equals("-1"))) {
                executeObjRefJdbcCall(entity, entry, jdbcCall, 0);
            }
        }
        return entity;
    }

    public void update(Entity entity) {
        executeObjectJdbcCall(entity, jdbcCall, 0, null);
        for (Map.Entry entry : entity.getAttributes().entrySet()) {
            executeAttributeJdbcCall(entity, entry, jdbcCall, 0);
        }
        for (Map.Entry entry : entity.getReferences().entrySet()) {
            if (!(entry.getValue().toString().equals("-1"))) {
                executeObjRefJdbcCall(entity, entry, jdbcCall, 0);
            }
        }
    }

    public void delete(Integer objectId, Integer forceDelete) {
        Entity temp = new Entity();
        temp.setObjectId(objectId);
        executeObjectJdbcCall(temp, jdbcCall, 1, forceDelete);
    }

    private RowMapper<Entity> rowMapper = new RowMapper<Entity>() {
        @Override
        public Entity mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Entity entity = new Entity();
            entity.setObjectId(resultSet.getInt("object_id"));
            entity.setObjectTypeId(resultSet.getInt("object_type_id"));
            entity.setParentId(resultSet.getInt("parent_id"));
            entity.setName(resultSet.getString("name"));
            entity.setDescription(resultSet.getString("description"));
            return entity;
        }
    };

    public Entity getById(Integer objectId) {
        Entity entity = jdbcTemplate.queryForObject(Query.SELECT_FROM_OBJECTS_BY_ID, rowMapper, objectId);
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(Query.SELECT_FROM_ATTRIBUTES_BY_ID, objectId);
        Map<Pair<Integer, Integer>, Object> attributes = getAttributes(rows);
        List<Map<String, Object>> rowsRef = jdbcTemplate.queryForList(Query.SELECT_FROM_OBJREFERENCE, objectId);
        Map<Pair<Integer, Integer>, Integer> references = getReferences(rowsRef);
        entity.setAttributes(attributes);
        entity.setReferences(references);
        return entity;
    }

    private Map<Pair<Integer, Integer>, Object> getAttributes(List<Map<String, Object>> rowss) {
        Map<Pair<Integer, Integer>, Object> attributes = new HashMap<>();
        for (Map row : rowss) {
            Integer attrId = new Integer(String.valueOf(row.get("attr_id")));
            Integer seq_no = new Integer(String.valueOf(row.get("seq_no")));
            Object value = null;

            if (row.get("value") != null) {
                value = String.valueOf(row.get("value"));
            } else if (row.get("date_value") != null) {
                value = String.valueOf(row.get("date_value"));
            }
            attributes.put(new Pair<>(attrId, seq_no), value);
        }
        return attributes;
    }


    private Map<Pair<Integer, Integer>, Integer> getReferences(List<Map<String, Object>> rowss) {
        Map<Pair<Integer, Integer>, Integer> reference = new HashMap<>();
        for (Map row : rowss) {
            Integer attrId = new Integer(String.valueOf(row.get("attrtype_id")));
            Integer seq_no = new Integer(String.valueOf(row.get("seq_no")));
            Integer value = null;
            if (row.get("reference") != null) {
                value = Integer.valueOf((String) row.get("reference"));
            }
            reference.put(new Pair<>(attrId, seq_no), value);
        }
        return reference;
    }

    public List<Entity> getAll(Integer objectTypeId) {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(Query.SELECT_FROM_OBJECTS, new Object[]{objectTypeId});
        if (rows.isEmpty()) return Collections.emptyList();
        List<Entity> entityList = new ArrayList<>();
        for (Map row : rows) {
            Entity entity = getById(Integer.valueOf((String) row.get("object_id")));
            entityList.add(entity);
        }
        return entityList;
    }

    public List<Entity> getBySQL(String sqlQuery) {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(Query.SELECT_FROM_OBJECTS_BY_SUBQUERY.concat("( ").concat(sqlQuery).concat(" )"));
        if (rows.isEmpty()) return Collections.emptyList();
        List<Entity> entityList = new ArrayList<>();
        for (Map row : rows) {
            Entity entity = getById(Integer.valueOf((String) row.get("object_id")));
            entityList.add(entity);
        }
        return entityList;
    }

    private Map<String, Object> executeObjectJdbcCall(Entity entity, SimpleJdbcCall jdbcCall, int delete, Integer forceDel) {
        jdbcCall.withProcedureName("UPDATE_OBJECT").withSchemaName("ODESSA_17").declareParameters(
                        new SqlParameter("p_OBJECT_ID", Types.NUMERIC),
                        new SqlParameter("p_PARENT_ID", Types.NUMERIC),
                        new SqlParameter("p_OBJECT_TYPE_ID", Types.NUMERIC),
                        new SqlParameter("p_NAME", Types.VARCHAR),

                        new SqlParameter("p_DESCRIPTION", Types.VARCHAR),
                        new SqlParameter("p_TO_DEL", Types.NUMERIC),
                        new SqlParameter("p_FORCE_DELETE", Types.NUMERIC)
                );
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("p_OBJECT_ID", entity.getObjectId() != null ? entity.getObjectId().toString() : null);
        if (entity.getParentId() == null) {
            inParam.put("p_PARENT_ID", null);
        } else {
            inParam.put("p_PARENT_ID", entity.getParentId().toString());
        }
        inParam.put("p_OBJECT_TYPE_ID", entity.getObjectTypeId() != null ? entity.getObjectTypeId().toString() : null);
        inParam.put("p_NAME", entity.getName());
        inParam.put("p_DESCRIPTION", entity.getDescription());
        inParam.put("p_TO_DEL", delete);
        inParam.put("p_FORCE_DELETE", forceDel);
        SqlParameterSource in = new MapSqlParameterSource(inParam);
        Connection connection;
        try {
            connection = jdbcTemplate.getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jdbcCall.execute(in);
    }

    private Map<String, Object> executeAttributeJdbcCall(Entity entity, Map.Entry entry, SimpleJdbcCall jdbcCall, int delete) {
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("P_OBJECT_ID", entity.getObjectId().toString());
        inParam.put("P_ATTR_ID", String.valueOf(((Pair) entry.getKey()).getKey()));
        Integer seq_no = (Integer) (((Pair) entry.getKey()).getValue());
        if (seq_no == 0) {
            inParam.put("P_SEQ_NO", null);
        } else {
            inParam.put("P_SEQ_NO", String.valueOf(seq_no));
        }
        if (entry.getValue().getClass().getSimpleName().equals("String")) {
            if (entry.getValue().equals("-1")) {
                inParam.put("p_VALUE", null);
            } else {
                inParam.put("p_VALUE", String.valueOf(entry.getValue()));
            }
            inParam.put("p_DATE_VALUE", null);

        } else if (entry.getValue().getClass().getSimpleName().equals("Date")) {
            inParam.put("p_VALUE", null);
            if (((Date) entry.getValue()).getTime() == new Date(-1).getTime()) {
                inParam.put("p_DATE_VALUE", null);
            } else {
                inParam.put("p_DATE_VALUE", new Timestamp(((Date) entry.getValue()).getTime()));
            }
        }
        inParam.put("p_TO_DEL", delete);
        jdbcCall.withProcedureName("UPDATE_ATTRIBUTE");
        SqlParameterSource in = new MapSqlParameterSource(inParam);
        return jdbcCall.execute(in);
    }

    private Map<String, Object> executeObjRefJdbcCall(Entity entity, Map.Entry entry, SimpleJdbcCall jdbcCall, int delete) {
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("P_OBJECT_ID", String.valueOf(entity.getObjectId()));
        inParam.put("P_ATTRTYPE_ID", String.valueOf(((Pair) entry.getKey()).getKey()));
        Integer seq_no = (Integer) ((Pair) entry.getKey()).getValue();
        if (seq_no == 0) {
            inParam.put("P_SEQ_NO", null);
        } else {
            inParam.put("P_SEQ_NO", String.valueOf(seq_no));
        }
        if (entry.getValue() == null) {
            inParam.put("P_OBJREFERENCE", null);
        } else {
            inParam.put("P_OBJREFERENCE", String.valueOf(entry.getValue()));
        }
        inParam.put("p_TO_DEL", delete);
        jdbcCall.withProcedureName("UPDATE_OBJREFERENCE");
        SqlParameterSource in = new MapSqlParameterSource(inParam);
        return jdbcCall.execute(in);
    }
}
