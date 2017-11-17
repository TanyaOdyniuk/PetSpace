package com.netcracker.dao.manager.query;

/**
 * Created by Odyniuk on 14/11/2017.
 */
public class CRUDQuery {
    public static final String CREATE_OBJECTS = "INSERT INTO OBJECTS" +
                                                "(parent_id, object_type_id, name, description)" +
                                                " VALUES(?,?,?,?)";
    public static final String CREATE_ATTRIBUTES = "INSERT INTO ATTRIBUTES" +
                                                "(value, date_value, seq_no, object_id, attr_id)" +
                                                " VALUES(?,?,?,?,?)";
    public static final String CREATE_OBJREFERENCE = "INSERT INTO OBJREFERENCE" +
                                                "(reference, object_id, attrtype_id, seq_no)" +
                                                " VALUES(?,?,?,?)";


    public static final String UPDATE_OBJECTS = "MERGE INTO OBJECTS " +
                                                "USING DUAL ON (object_id=?) "+
                                                "WHEN NOT MATCHED THEN " +
                                                "INSERT (parent_id,object_type_id,name,description,object_id) " +
                                                "VALUES (?,?,?,?,?) " +
                                                "WHEN MATCHED THEN " +
                                                "UPDATE SET parent_id=?, object_type_id=?, name=?, description=?";
    public static final String UPDATE_OBJREFERENCE = "MERGE INTO OBJREFERENCE " +
                                                "USING DUAL ON (object_id=? AND attrtype_id =? AND seq_no =?) "+
                                                "WHEN NOT MATCHED THEN " +
                                                "INSERT (reference, object_id, attrtype_id, seq_no) " +
                                                "VALUES(?,?,?,?)"+
                                                "WHEN MATCHED THEN " +
                                                "UPDATE SET reference=?";
    public static final String UPDATE_ATTRIBUTES = "MERGE INTO ATTRIBUTES " +
                                                "USING DUAL ON (object_id=? AND attr_id =? AND  seq_no =?) "+
                                                "WHEN NOT MATCHED THEN " +
                                                "INSERT (value, date_value, object_id, attr_id, seq_no )" +
                                                "VALUES(?,?,?,?,?)"+
                                                "WHEN MATCHED THEN " +
                                                "UPDATE SET  value=?, date_value=?";


    public static final String DELETE_FROM_ATTRIBUTES = "DELETE " +
                                                "FROM ATTRIBUTES " +
                                                "WHERE object_id=?";
    public static final String DELETE_FROM_OBJREFERENCE = "DELETE " +
                                                "FROM OBJREFERENCE " +
                                                "WHERE object_id=?";
    public static final String DELETE_FROM_OBJECTS = "DELETE " +
                                                "FROM OBJECTS " +
                                                "WHERE object_id=?";


    public static final String SELECT_FROM_OBJECTS_BY_ID = "SELECT * " +
                                                "FROM OBJECTS " +
                                                "WHERE object_id=?";
    public static final String SELECT_FROM_ATTRIBUTES_BY_ID = "SELECT value, date_value, seq_no, attr_id " +
                                                "from  attributes" +
                                                "WHERE OBJECT_ID = ?";
    public static final String SELECT_FROM_OBJREFERENCE =  "SELECT * " +
                                                "FROM OBJREFERENCE " +
                                                "WHERE object_id=?";
    public static final String SELECT_FROM_OBJECTS = "SELECT * " +
                                                "FROM Objects " +
                                                "WHERE object_type_id = ?";
}
