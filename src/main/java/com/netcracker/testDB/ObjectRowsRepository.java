package com.netcracker.testDB;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
class ObjectRowsRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    List<String> findAllObjectsHierarchy() {

        return jdbcTemplate.query(
                " SELECT lpad('_', 10*(level-1), '_') || code AS " +
                        "\"PetSpace Objects Hierarchy\" " +
                        "FROM objtype " +
                        "START WITH parent_id is null " +
                        "CONNECT BY prior object_type_id = parent_id",
                (rs, rowNum) -> rs.getString("PetSpace Objects Hierarchy")
        );
    }

    // thanks Java 8, look the custom RowMapper
    List<ObjectRow> findAll() {

        return jdbcTemplate.query(
                " select object_type_id, parent_id, code, name" +
                        " from objtype" +
                        " order by object_type_id",
                (rs, rowNum) -> new ObjectRow(rs.getInt("object_type_id"),
                        rs.getInt("parent_id"),
                        rs.getString("code"),
                        rs.getString("name"))
        );
    }
}