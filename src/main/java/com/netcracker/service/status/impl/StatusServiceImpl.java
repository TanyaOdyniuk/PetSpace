package com.netcracker.service.status.impl;

import com.netcracker.dao.manager.query.QueryDescriptor;
import com.netcracker.dao.managerservice.EntityManagerService;
import com.netcracker.model.status.Status;
import com.netcracker.service.status.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatusServiceImpl implements StatusService {

    @Autowired
    private EntityManagerService entityManagerService;

    public Status getActiveStatus(){
        String getStatusSql = "select  OBJECT_ID from attributes" +
                " where value = 'active'";
        return entityManagerService.getObjectsBySQL(getStatusSql, Status.class, new QueryDescriptor())
                .iterator().next();
    }

}
