package com.netcracker.controller.securitybook;

import com.netcracker.model.securitybook.SecurityType;
import com.netcracker.service.securitybook.SecurityBookService;
import com.netcracker.service.securitybook.impl.SecurityBookServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/securitybook")
public class SecurityBookController {

    @Autowired
    SecurityBookServiceImpl service;

    @GetMapping("/types")
    public List<SecurityType> getSecurityTypes() {
        return service.getAllSecurityTypes();
    }
}
