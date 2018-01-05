package com.netcracker.service.securitybook.impl;

import com.netcracker.dao.manager.query.QueryDescriptor;
import com.netcracker.dao.managerservice.EntityManagerService;
import com.netcracker.model.securitybook.SecurityBookConstant;
import com.netcracker.model.securitybook.SecurityType;
import com.netcracker.service.securitybook.SecurityBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class SecurityBookServiceImpl implements SecurityBookService{

    @Autowired
    EntityManagerService entityManagerService;

    @Override
    public List<SecurityType> getAllSecurityTypes() {
        return entityManagerService.getAll(BigInteger.valueOf(SecurityBookConstant.SECT_TYPE), SecurityType.class, new QueryDescriptor());
    }
}
