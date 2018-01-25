package com.netcracker.security;

import com.netcracker.service.user.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    /*
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("admin").password("admin").roles("ADMIN")
                .and()
                .withUser("user").password("user").roles("USER");
    }
    */

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/authorization"))
                .and()
                .authorizeRequests()
                .antMatchers("/").authenticated()
                .antMatchers("/main").authenticated()
//                .antMatchers("/users/**").authenticated()
//                .antMatchers("/bulletinboard/**").authenticated()
//                .antMatchers("/category/**").authenticated()
//                .antMatchers("/comments/**").authenticated()
////                .antMatchers("/friends/**").authenticated()
//                .antMatchers("/albums/**").authenticated()
//                .antMatchers("/gallery/**").authenticated()
//                .antMatchers("/groups/**").authenticated()
//                .antMatchers("/groupList/**").authenticated()
//                .antMatchers("/likes/**").authenticated()
//                .antMatchers("/message/**").authenticated()
//                .antMatchers("/news/**").authenticated()
////                .antMatchers("/pets/**").authenticated()
////                .antMatchers("/pet/**").authenticated()
////                .antMatchers("/profile/**").authenticated()
////                .antMatchers("/records/**").authenticated()
////                .antMatchers("/request/**").authenticated()
//                .antMatchers("/securitybook/**").authenticated()
//                .antMatchers("/species/**").authenticated()
                .antMatchers("/admin*").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/authorization").permitAll()
                .antMatchers("/login").permitAll()
                .and()
                .formLogin().disable()
                .logout().disable();
    }
}
