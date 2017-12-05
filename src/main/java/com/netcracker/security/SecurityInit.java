package com.netcracker.security;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;


public class SecurityInit extends AbstractSecurityWebApplicationInitializer {
    protected SecurityInit(Class<?>... configurationClasses) {
        super(SecurityConfig.class);
    }
}
