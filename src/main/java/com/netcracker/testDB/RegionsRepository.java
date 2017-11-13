package com.netcracker.testDB;

import com.netcracker.testDB.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RegionsRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // thanks Java 8, look the custom RowMapper
    public List<Region> findAll() {

        List<Region> result = jdbcTemplate.query(
                "SELECT region_id, region_name FROM regions",
                (rs, rowNum) -> new Region(rs.getInt("region_id"),
                        rs.getString("region_name"))
        );

        return result;

    }

}