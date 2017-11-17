package com.netcracker.testDB;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ObjectRowsRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<String> findAllObjectsHierarchy() {

        List<String> result = jdbcTemplate.query(
                " SELECT lpad('_', 10*(level-1), '_') || code AS " +
                        "\"PetSpace Objects Hierarchy\" " +
                        "FROM objtype " +
                        "START WITH parent_id is null " +
                        "CONNECT BY prior object_type_id = parent_id",
                (rs, rowNum) -> rs.getString("PetSpace Objects Hierarchy")
        );
        return result;
    }

    // thanks Java 8, look the custom RowMapper
    public List<ObjectRow> findAll() {

        List<ObjectRow> result = jdbcTemplate.query(
                " select object_type_id, parent_id, code, name from objtype",
                (rs, rowNum) -> new ObjectRow(rs.getInt("object_type_id"),
                        rs.getInt("parent_id"),
                        rs.getString("code"),
                        rs.getString("name"))
        );

        return result;
    }
}