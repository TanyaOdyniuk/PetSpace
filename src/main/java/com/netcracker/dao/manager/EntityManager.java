package com.netcracker.dao.manager;

import com.netcracker.dao.Entity;
import com.netcracker.dao.manager.query.CRUDQuery;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.*;
import java.sql.Date;
import java.util.*;

import static java.sql.Types.DATE;
import static java.sql.Types.NULL;
import static java.sql.Types.NUMERIC;

/**
 * Created by Odyniuk on 15/11/2017.
 */

@Component
public class EntityManager {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public EntityManager(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Transactional
    public Entity create(Entity entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps =
                        connection.prepareStatement(CRUDQuery.CREATE_OBJECTS, new String[]{"object_id"});
                int i = 0;
                if (entity.getParentId() == null) {
                    ps.setNull(++i, NUMERIC);
                } else {
                    ps.setString(++i, entity.getParentId().toString());
                }
                ps.setString(++i, entity.getObjectTypeId().toString());
                ps.setString(++i, entity.getName());
                ps.setString(++i, entity.getDescription());
                return ps;
            }
        }, keyHolder);
        entity.setObjectId(keyHolder.getKey().intValue());
        for (Map.Entry entry : entity.getReferences().entrySet()) {
            if (!(entry.getValue().toString().equals("-1"))) {
                jdbcTemplate.update(CRUDQuery.CREATE_OBJREFERENCE, getPreparedStatementSetterRef(entry, entity, true));
            }
        }
        return entity;
    }

    public void update(Entity entity) {
        jdbcTemplate.update(CRUDQuery.UPDATE_OBJECTS, getPreparedStatementSetterObjects(entity));
        for (Map.Entry entry : entity.getAttributes().entrySet()) {
            jdbcTemplate.update(CRUDQuery.UPDATE_ATTRIBUTES, getPreparedStatementSetterAttributes(entry, entity, false));
        }
        for (Map.Entry entry : entity.getReferences().entrySet()) {
            if (!(entry.getValue().toString().equals("-1"))) {
                jdbcTemplate.update(CRUDQuery.UPDATE_OBJREFERENCE, getPreparedStatementSetterRef(entry, entity, false));
            }
        }
    }

    public void delete(Integer objectId) {
        jdbcTemplate.update(CRUDQuery.DELETE_FROM_OBJECTS, objectId);
        jdbcTemplate.update(CRUDQuery.DELETE_FROM_ATTRIBUTES, objectId);
        jdbcTemplate.update(CRUDQuery.DELETE_FROM_OBJREFERENCE, objectId);
        jdbcTemplate.update("COMMIT");
    }

    private RowMapper<Entity> rowMapper = new RowMapper<Entity>() {
        @Override
        public Entity mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Entity entity = new Entity();
            entity.setObjectId(resultSet.getInt("object_id"));
            entity.setObjectTypeId(resultSet.getInt("object_type_id"));
            entity.setParentId(resultSet.getInt("parent_id")); /**/
            entity.setName(resultSet.getString("name"));
            entity.setDescription(resultSet.getString("description"));
            return entity;
        }
    };

    public Entity getById(Integer objectId) {
        Entity entity = jdbcTemplate.queryForObject(CRUDQuery.SELECT_FROM_OBJECTS_BY_ID, rowMapper, objectId);
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(CRUDQuery.SELECT_FROM_ATTRIBUTES_BY_ID, objectId);
        Map<Pair<Integer, Integer>, Object> attributes = getAttributes(rows);
        List<Map<String, Object>> rowsRef = jdbcTemplate.queryForList(CRUDQuery.SELECT_FROM_OBJREFERENCE, objectId);
        Map<Pair<Integer, Integer>, Integer> references = getReferences(rowsRef);
        entity.setAttributes(attributes);
        entity.setReferences(references);
        return entity;
    }

    private Map<Pair<Integer, Integer>, Object> getAttributes(List<Map<String, Object>> rowss) {
        Map<Pair<Integer, Integer>, Object> attributes = new HashMap();
        for (Map row : rowss) {
            Integer attrId = new Integer(String.valueOf(row.get("attr_id")));
            Integer seq_no = new Integer(String.valueOf(row.get("seq_no")));
            Object value = null;

            if (row.get("value") != null) {
                value = String.valueOf(row.get("value"));
            } else if (row.get("date_value") != null) {
                value = String.valueOf(row.get("date_value"));
            }
            attributes.put(new Pair(attrId, seq_no), value);
        }
        return attributes;
    }

    private Map<Pair<Integer, Integer>, Integer> getReferences(List<Map<String, Object>> rowss) {
        Map<Pair<Integer, Integer>, Integer> reference = new HashMap();
        for (Map row : rowss) {
            Integer attrId = new Integer(String.valueOf(row.get("attrtype_id")));
            Integer seq_no = new Integer(String.valueOf(row.get("seq_no")));
            Integer value = null;
            if (row.get("reference") != null) {
                value = Integer.valueOf((String) row.get("reference"));
            }
            reference.put(new Pair(attrId, seq_no), value);
        }
        return reference;
    }

    public List<Entity> getAll(Integer objectTypeId) {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(CRUDQuery.SELECT_FROM_OBJECTS, new Object[]{objectTypeId});
        if (rows.isEmpty()) return Collections.emptyList();
        List<Entity> entityList = new ArrayList<>();
        for (Map row : rows) {
            Entity entity = getById(Integer.valueOf((String) row.get("object_id")));
            entityList.add(entity);
        }
        return entityList;
    }

    public List<Entity> getBySQL(String sqlQuery, List<String> params) {
        return null;
    }

    private PreparedStatementSetter getPreparedStatementSetterRef(Map.Entry entry, Entity entity, boolean create) {
        return new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                int i = 0;
                if (create) {
                    if (entry.getValue() == null) {
                        ps.setNull(++i, NULL);
                    } else {
                        ps.setString(++i, String.valueOf(entry.getValue()));
                    }
                    ps.setString(++i, String.valueOf(entity.getObjectId()));
                    ps.setString(++i, String.valueOf(((Pair)entry.getKey()).getKey()));
                    ps.setString(++i, String.valueOf(((Pair)entry.getKey()).getValue()));
                } else {
                    ps.setString(++i, String.valueOf(entity.getObjectId()));
                    ps.setString(++i, String.valueOf(((Pair)entry.getKey()).getKey()));
                    ps.setString(++i, String.valueOf(((Pair)entry.getKey()).getValue()));
                    if (entry.getValue() == null) {
                        ps.setNull(++i, NULL);
                    } else {
                        ps.setString(++i, String.valueOf(entry.getValue()));
                    }
                    ps.setString(++i, String.valueOf(entity.getObjectId()));
                    ps.setString(++i, String.valueOf(((Pair)entry.getKey()).getKey()));
                    ps.setString(++i, String.valueOf(((Pair)entry.getKey()).getValue()));
                    if (entry.getValue() == null) {
                        ps.setNull(++i, NULL);
                    } else {
                        ps.setString(++i, String.valueOf(entry.getValue()));
                    }
                }
            }
        };
    }

    private PreparedStatementSetter getPreparedStatementSetterObjects(Entity entity) {
        return new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                int i = 0;
                ps.setString(++i, entity.getObjectId().toString());
                if (entity.getParentId() == null) {
                    ps.setNull(++i, NUMERIC);
                } else {
                    ps.setString(++i, entity.getParentId().toString());
                }
                ps.setString(++i, entity.getObjectTypeId().toString());
                ps.setString(++i, entity.getName());
                ps.setString(++i, entity.getDescription());
                ps.setString(++i, entity.getObjectId().toString());
                if (entity.getParentId() == null) {
                    ps.setNull(++i, NUMERIC);
                } else {
                    ps.setString(++i, entity.getParentId().toString());
                }
                ps.setString(++i, entity.getObjectTypeId().toString());
                ps.setString(++i, entity.getName());
                ps.setString(++i, entity.getDescription());
            }
        };
    }

    private PreparedStatementSetter getPreparedStatementSetterAttributes(Map.Entry entry, Entity entity, boolean create) {
        return new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                Integer attrId = new Integer(String.valueOf(((Pair)entry.getKey()).getKey()));
                Integer seq_no = new Integer(String.valueOf((((Pair)entry.getKey()).getValue())));
                if (create) {
                    prepareValue(ps, entry, 0);
                    ps.setString(3, entity.getObjectId().toString());
                    ps.setString(4, attrId.toString());
                    ps.setString(5, seq_no.toString());
                } else {
                    ps.setString(1, entity.getObjectId().toString());
                    ps.setString(2, attrId.toString());
                    ps.setString(3, seq_no.toString());
                    prepareValue(ps, entry, 3);
                    ps.setString(6, entity.getObjectId().toString());
                    ps.setString(7, attrId.toString());
                    ps.setString(8, seq_no.toString());
                }
            }

            private void prepareValue(PreparedStatement ps, Map.Entry entry, int i) throws SQLException {
                if (entry.getValue().getClass().getSimpleName().equals("String")) {
                    if (entry.getValue().equals("-1")) {
                        ps.setNull(++i, NULL);
                    } else {
                        ps.setString(++i, String.valueOf(entry.getValue()));
                    }
                    ps.setNull(++i, DATE);
                    ps.setNull(++i, NUMERIC);

                } else if (entry.getValue().getClass().getSimpleName().equals("Date")) {
                    ps.setString(++i, null);
                    if (((Date) entry.getValue()).getTime() == new Date(-1).getTime()) {
                        ps.setNull(++i, NULL);
                    } else {
                        ps.setTimestamp(++i, new Timestamp(((Date) entry.getValue()).getTime()));
                    }
                    ps.setNull(++i, NUMERIC);

                }
            }

        };

    }
}