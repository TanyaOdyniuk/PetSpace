package com.netcracker.controller;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.Hashtable;

public class DBResource {

    private DataSource ds;

    public DBResource() {
        try {
/*            Context ctx = new InitialContext();
            ctx.addToEnvironment("java.naming.factory.initial","com.sun.jndi.fscontext.RefFSContextFactory");
            ds = (DataSource) ctx.lookup("java:comp/env/jdbc/myDS");*/

            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            ds = (DataSource)
                    envCtx.lookup("jdbc/myDS");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public void printDS() {
        System.out.println("DataSource = " + ds);
    }
}
