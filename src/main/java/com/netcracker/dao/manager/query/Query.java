package com.netcracker.dao.manager.query;

public interface Query {

    String SELECT_FROM_OBJECTS_BY_ID = "SELECT * " +
            "FROM OBJECTS " +
            "WHERE object_id=?";
    String SELECT_FROM_ATTRIBUTES_BY_ID = "SELECT value, date_value, nvl(seq_no,0) as sn, attrtype_id " +
            "from  attributes " +
            "WHERE OBJECT_ID = ?";
    String SELECT_FROM_OBJREFERENCE =  "SELECT object_id, attrtype_id, nvl(seq_no, 0) as sn, reference " +
            "FROM OBJREFERENCE " +
            "WHERE object_id=? or reference =?";
    String SELECT_FROM_OBJECTS = "SELECT * " +
            "FROM Objects " +
            "WHERE object_type_id = ?";

    String SELECT_FROM_OBJECTS_BY_SUBQUERY = "SELECT * " +
            "FROM Objects " +
            "WHERE object_id in ";
}
