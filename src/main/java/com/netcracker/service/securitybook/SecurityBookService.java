package com.netcracker.service.securitybook;

import com.netcracker.model.securitybook.SecurityType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SecurityBookService {
    List<SecurityType> getAllSecurityTypes();
}
