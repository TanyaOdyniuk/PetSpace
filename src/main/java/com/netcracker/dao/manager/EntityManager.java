package com.netcracker.dao.manager;

import com.netcracker.dao.manager.query.QueryBuilder;
import com.netcracker.dao.Entity;
import com.netcracker.dao.manager.query.Query;
import com.netcracker.dao.manager.query.QueryDescriptor;
import javafx.util.Pair;
import oracle.jdbc.OracleTypes;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlInOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class EntityManager {
    @Autowired
    private QueryBuilder queryBuilder;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public EntityManager(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);
    }

    public Integer getNextSeqNoObjRef(BigInteger obj_id, BigInteger ref_id, BigInteger attr_type_id) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate);
        jdbcCall.withFunctionName("get_next_seq_no_objreference");
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_OBJ_ID", obj_id)
                .addValue("p_REF_ID", ref_id)
                .addValue("p_ATTR_TYPE_ID", attr_type_id);
        BigDecimal res = jdbcCall.executeFunction(BigDecimal.class, in);
        return res.intValue();
    }

    public Integer getNextSeqNoObjAttributes(BigInteger obj_id, BigInteger attr_type_id) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate);
        jdbcCall.withFunctionName("get_next_seq_no_attributes");
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_OBJ_ID", obj_id)
                .addValue("p_ATTR_TYPE_ID", attr_type_id);
        BigDecimal res = jdbcCall.executeFunction(BigDecimal.class, in);
        return res.intValue();
    }

    public BigInteger getNextObjId() {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate);
        jdbcCall.withFunctionName("check_id");
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_ID", null);
        BigDecimal id = jdbcCall.executeFunction(BigDecimal.class, in);
        return id.toBigInteger();
    }

    @Transactional
    public Entity create(Entity entity) {
        executeObjectJdbcCall(entity, 0, null);

        for (Map.Entry entry : entity.getAttributes().entrySet()) {
            if (!(entry.getValue().toString().equals("-1")) && entry.getValue() != null) {
                executeAttributeJdbcCall(entity, entry);
            }
        }

        for (Map.Entry entry : entity.getReferences().entrySet()) {
            Pair<BigInteger, BigInteger> objIdRef = (Pair<BigInteger, BigInteger>) entry.getValue();
            if (objIdRef.getKey() != null && objIdRef.getValue() != null
                    && !(objIdRef.getKey().toString().equals("-1"))
                    && !(objIdRef.getValue().toString().equals("-1"))) {
                executeObjRefJdbcCall(entry);
            }
        }
        return entity;
    }

    public void update(Entity entity) {
        executeObjectJdbcCall(entity, 0, null);

        for (Map.Entry entry : entity.getAttributes().entrySet()) {
            executeAttributeJdbcCall(entity, entry);
        }

        for (Map.Entry entry : entity.getReferences().entrySet()) {
            Pair<BigInteger, BigInteger> objIdRef = (Pair<BigInteger, BigInteger>) entry.getValue();
            if (objIdRef.getKey() != null && objIdRef.getValue() != null
                    && !(objIdRef.getKey().toString().equals("-1"))
                    && !(objIdRef.getValue().toString().equals("-1"))) {
                executeObjRefJdbcCall(entry);
            }
        }
    }

    public void delete(BigInteger objectId, Integer forceDelete) {
        Entity temp = new Entity();
        temp.setObjectId(objectId);
        executeObjectJdbcCall(temp, 1, forceDelete);
    }

    private RowMapper<Entity> rowMapper = new RowMapper<Entity>() {
        @Override
        public Entity mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Entity entity = new Entity();
            entity.setObjectId(BigInteger.valueOf(resultSet.getLong("object_id")));
            entity.setObjectTypeId(BigInteger.valueOf(resultSet.getLong("object_type_id")));
            Long parentId = resultSet.getLong("parent_id");
            entity.setParentId(parentId != 0 ? BigInteger.valueOf(parentId) : null);
            entity.setName(resultSet.getString("name"));
            entity.setDescription(resultSet.getString("description"));
            return entity;
        }
    };

    public Entity getById(BigInteger objectId) {
        Entity entity = jdbcTemplate.queryForObject(Query.SELECT_FROM_OBJECTS_BY_ID, rowMapper, objectId);
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(Query.SELECT_FROM_ATTRIBUTES_BY_ID, objectId);
        Map<Pair<BigInteger, Integer>, Object> attributes = getAttributes(rows);
        List<Map<String, Object>> rowsRef = jdbcTemplate.queryForList(Query.SELECT_FROM_OBJREFERENCE, objectId, objectId);
        Map<Pair<BigInteger, Integer>, Pair<BigInteger, BigInteger>> references = getReferences(rowsRef, objectId);
        entity.setAttributes(attributes);
        entity.setReferences(references);
        return entity;
    }

    public Entity getByIdRef(BigInteger objectId) {
        Entity entity = jdbcTemplate.queryForObject(Query.SELECT_FROM_OBJECTS_BY_ID, rowMapper, objectId);
        entity.setAttributes(new HashMap<>());
        entity.setReferences(new HashMap<>());
        return entity;
    }

    private Map<Pair<BigInteger, Integer>, Object> getAttributes(List<Map<String, Object>> rowss) {
        Map<Pair<BigInteger, Integer>, Object> attributes = new HashMap<>();
        for (Map row : rowss) {
            BigInteger attrId = ((BigDecimal) row.get("attrtype_id")).toBigInteger();
            BigDecimal sId = (BigDecimal) row.get("sn");
            Integer seq_no = sId.intValueExact();
            Object value = null;

            if (row.get("value") != null) {
                value = row.get("value");
            } else if (row.get("date_value") != null) {
                value = row.get("date_value");
            }
            attributes.put(new Pair<>(attrId, seq_no), value);
        }
        return attributes;
    }

    private Map<Pair<BigInteger, Integer>, Pair<BigInteger, BigInteger>> getReferences(List<Map<String, Object>> rowss, BigInteger curObjId) {
        Map<Pair<BigInteger, Integer>, Pair<BigInteger, BigInteger>> reference = new HashMap<>();
        for (Map row : rowss) {
            BigInteger attrId = ((BigDecimal) row.get("attrtype_id")).toBigInteger();
            BigDecimal sId = (BigDecimal) row.get("sn");
            Integer seq_no = sId.intValueExact();
            BigInteger obId = null;
            BigInteger refId = null;
            BigInteger objectFromDB = ((BigDecimal) row.get("object_id")).toBigInteger();
            BigInteger refFromDB = ((BigDecimal) row.get("reference")).toBigInteger();
            if (objectFromDB != null && !curObjId.equals(objectFromDB)) {
                obId = objectFromDB;
                refId = curObjId;
            } else if (refFromDB != null && !curObjId.equals(refFromDB)) {
                refId = refFromDB;
                obId = curObjId;
            }
            reference.put(new Pair<>(attrId, seq_no), new Pair<>(obId, refId));
        }
        return reference;
    }

    public List<Entity> getAll(BigInteger objectTypeId, QueryDescriptor queryDescriptor) {
        queryDescriptor.setInnerQuery(Query.SELECT_FROM_OBJECTS);
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                queryBuilder.build(queryDescriptor),
                objectTypeId);
        if (rows.isEmpty()) return Collections.emptyList();
        List<Entity> entityList = new ArrayList<>();
        for (Map row : rows) {
            Entity entity = getById(((BigDecimal) row.get("object_id")).toBigInteger());
            entityList.add(entity);
        }
        return entityList;
    }

    public List<Entity> getBySQL(String sqlQuery, QueryDescriptor queryDescriptor) {
        queryDescriptor.setInnerQuery(Query.SELECT_FROM_OBJECTS_BY_SUBQUERY.concat("( ").concat(sqlQuery).concat(" )"));
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                queryBuilder.build(queryDescriptor));
        if (rows.isEmpty()) return Collections.emptyList();
        List<Entity> entityList = new ArrayList<>();
        for (Map row : rows) {
            Entity entity = getById(((BigDecimal) row.get("object_id")).toBigInteger());
            entityList.add(entity);
        }
        return entityList;
    }

    public int getAllCount(BigInteger objectTypeId) {
        return jdbcTemplate.queryForObject(QueryBuilder.buildCountQuery(Query.SELECT_FROM_OBJECTS),
                (resultSet, i) -> {
                    return resultSet.getInt("c");
                },
                objectTypeId);
    }

    public int getBySqlCount(String sqlQuery) {
        return jdbcTemplate.queryForObject(QueryBuilder.buildCountQuery(Query.SELECT_FROM_OBJECTS_BY_SUBQUERY.concat("( ").concat(sqlQuery).concat(" )")),
                (resultSet, i) -> {
                    return resultSet.getInt("c");
                });
    }
    public void dropRef(int attr_id, BigInteger obj, int flag){
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate);
        jdbcCall.withProcedureName("DROPOBJREF").declareParameters(
                new SqlParameter("attr", OracleTypes.NUMBER),
                new SqlParameter("obj", OracleTypes.NUMBER),
                new SqlParameter("flag", OracleTypes.NUMBER));
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("attr", attr_id);
        inParam.put("obj", obj);
        inParam.put("flag", flag);
        SqlParameterSource in = new MapSqlParameterSource(inParam);
        jdbcCall.execute(in);
    }
    public void dropRefDual(int attr_id, BigInteger ref, BigInteger obj){
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate);
        jdbcCall.withProcedureName("DROPOBJREFDUAL").declareParameters(
                new SqlParameter("attr", OracleTypes.NUMBER),
                new SqlParameter("refer", OracleTypes.NUMBER),
                new SqlParameter("obj", OracleTypes.NUMBER));
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("attr", attr_id);
        inParam.put("refer", ref);
        inParam.put("obj", obj);
        SqlParameterSource in = new MapSqlParameterSource(inParam);
        jdbcCall.execute(in);
    }
    public void insertObjref(int attr_id, BigInteger ref, BigInteger obj){
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate);
        jdbcCall.withProcedureName("INSERTOBJREF").declareParameters(
                new SqlParameter("attr", OracleTypes.NUMBER),
                new SqlParameter("refer", OracleTypes.NUMBER),
                new SqlParameter("obj", OracleTypes.NUMBER));
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("attr", attr_id);
        inParam.put("refer", ref);
        inParam.put("obj", obj);
        SqlParameterSource in = new MapSqlParameterSource(inParam);
        jdbcCall.execute(in);
    }

    private Map<String, Object> executeObjectJdbcCall(Entity entity, int delete, Integer forceDel) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate);
        jdbcCall.withProcedureName("UPDATE_OBJ").declareParameters(
                new SqlInOutParameter("p_OBJECT_ID", OracleTypes.NUMBER),
                new SqlParameter("p_PARENT_ID", OracleTypes.NUMBER),
                new SqlParameter("p_OBJECT_TYPE_ID", OracleTypes.NUMBER),
                new SqlParameter("p_NAME", OracleTypes.VARCHAR),
                new SqlParameter("p_DESCRIPTION", OracleTypes.VARCHAR),
                new SqlParameter("p_TO_DEL", OracleTypes.INTEGER),
                new SqlParameter("p_FORCE_DELETE", OracleTypes.INTEGER));
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("p_OBJECT_ID", entity.getObjectId());
        inParam.put("p_PARENT_ID", entity.getParentId());
        inParam.put("p_OBJECT_TYPE_ID", entity.getObjectTypeId());
        inParam.put("p_NAME", entity.getName());
        inParam.put("p_DESCRIPTION", entity.getDescription());
        inParam.put("p_TO_DEL", delete);
        inParam.put("p_FORCE_DELETE", forceDel);
        SqlParameterSource in = new MapSqlParameterSource(inParam);
        return jdbcCall.execute(in);
    }

    private Map<String, Object> executeAttributeJdbcCall(Entity entity, Map.Entry entry) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate);
        Map<String, Object> inParam = new HashMap<>();
        inParam.put("P_OBJECT_ID", entity.getObjectId());
        inParam.put("P_ATTR_ID", ((Pair) entry.getKey()).getKey());
        Integer seq_no = (Integer) (((Pair) entry.getKey()).getValue());
        inParam.put("P_SEQ_NO", seq_no == 0 ? null : BigInteger.valueOf(seq_no));
        String entryClassName = entry.getValue().getClass().getSimpleName();
        switch (entryClassName) {
            case "Date":
                inParam.put("p_VALUE", null);
                if (((Date) entry.getValue()).getTime() == new Date(-1).getTime()) {
                    inParam.put("p_DATE_VALUE", null);
                } else {
                    inParam.put("p_DATE_VALUE", new Timestamp(((Date) entry.getValue()).getTime()));
                }
                break;
            case "Timestamp":
                inParam.put("p_VALUE", null);
                if (((Timestamp) entry.getValue()).getTime() == new Date(-1).getTime()) {
                    inParam.put("p_DATE_VALUE", null);
                } else {
                    inParam.put("p_DATE_VALUE", entry.getValue());
                }
                break;
            default:
                if (entry.getValue().equals("-1")) {
                    inParam.put("p_VALUE", null);
                } else {
                    inParam.put("p_VALUE", entry.getValue());
                }
                inParam.put("p_DATE_VALUE", null);
                break;
        }
        inParam.put("p_TO_DEL", 0);
        jdbcCall.withProcedureName("UPDATE_ATTRIBUTE").declareParameters(
                new SqlParameter("p_OBJECT_ID", OracleTypes.NUMBER),
                new SqlParameter("P_ATTR_ID", OracleTypes.NUMBER),
                new SqlParameter("P_SEQ_NO", OracleTypes.NUMBER),
                new SqlParameter("p_VALUE", OracleTypes.VARCHAR),
                new SqlParameter("p_DATE_VALUE", OracleTypes.DATE),
                new SqlParameter("p_TO_DEL", OracleTypes.INTEGER)
        );
        SqlParameterSource in = new MapSqlParameterSource(inParam);
        return jdbcCall.execute(in);
    }

    private Map<String, Object> executeObjRefJdbcCall(Map.Entry entry) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate);
        Map<String, Object> inParam = new HashMap<>();
        Pair<BigInteger, BigInteger> objIdRef = (Pair<BigInteger, BigInteger>) entry.getValue();
        inParam.put("P_OBJECT_ID", objIdRef.getKey());
        inParam.put("P_ATTRTYPE_ID", ((Pair) entry.getKey()).getKey());
        Integer seq_no = (Integer) ((Pair) entry.getKey()).getValue();
        inParam.put("P_SEQ_NO", seq_no == 0 ? null : BigInteger.valueOf(seq_no));
        inParam.put("P_OBJREFERENCE", objIdRef.getValue());
        inParam.put("p_TO_DEL", 0);
        jdbcCall.withProcedureName("UPDATE_OBJREFERENCE").declareParameters(
                new SqlParameter("P_OBJECT_ID", OracleTypes.NUMBER),
                new SqlParameter("P_ATTRTYPE_ID", OracleTypes.NUMBER),
                new SqlParameter("P_SEQ_NO", OracleTypes.NUMBER),
                new SqlParameter("P_OBJREFERENCE", OracleTypes.NUMBER),
                new SqlParameter("p_TO_DEL", OracleTypes.NUMBER)
        );
        SqlParameterSource in = new MapSqlParameterSource(inParam);
        return jdbcCall.execute(in);
    }
}