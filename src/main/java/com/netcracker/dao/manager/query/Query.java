package com.netcracker.dao.manager.query;

public interface Query {
    String IGNORING_DELETED_ELEMENTS = "OBJECT_ID NOT IN " +
            "(SELECT o.object_id FROM objreference o " +
            "WHERE o.ATTRTYPE_ID IN (SELECT a AS ATTRTYPE_ID FROM STATE_ATTR_TYPES) " +
            "AND o.REFERENCE = 9)";
    String SELECT_FROM_OBJECTS_BY_ID = "SELECT * " +
            "FROM OBJECTS " +
            "WHERE object_id=? " +
            "and " + IGNORING_DELETED_ELEMENTS;
    String SELECT_FROM_ATTRIBUTES_BY_ID = "SELECT value, date_value, nvl(seq_no,0) as sn, attrtype_id " +
            "from  attributes " +
            "WHERE OBJECT_ID = ? " +
            "and " + IGNORING_DELETED_ELEMENTS;
    String SELECT_FROM_OBJREFERENCE =  "SELECT object_id, attrtype_id, nvl(seq_no, 0) as sn, reference " +
            "FROM OBJREFERENCE " +
            "WHERE object_id=? or reference =? " +
            "and " + IGNORING_DELETED_ELEMENTS;
    String SELECT_FROM_OBJECTS = "SELECT * " +
            "FROM Objects " +
            "WHERE object_type_id = ? " +
            "and " + IGNORING_DELETED_ELEMENTS;

    String SELECT_FROM_OBJECTS_BY_SUBQUERY = "SELECT * " +
            "FROM Objects " +
            "WHERE "  + IGNORING_DELETED_ELEMENTS +
            " and object_id in ";
}
