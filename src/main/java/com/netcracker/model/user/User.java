package com.netcracker.model.user;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.model.BaseEntity;
import com.netcracker.model.securitybook.SecurityBook;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@ObjectType(value = 1)
public class User extends BaseEntity implements UserDetails {

    @Attribute(value = 1)
    private String login;
    @Attribute(value = 2)
    private String password;
    @Attribute(value = 3)
    private Profile profile;
    @Attribute(value = 4)
    private List<UserAuthority> userAuthorities;
    //TODO SERVICE GETSECURITYBOOKS
    @Attribute(value = 5)
    private Set<SecurityBook> securityBooks;

    //SECURITY VARIABLES
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    public User() {
    }

    public User(String name) {
        super(name);
    }

    public User(String name, String description) {
        super(name, description);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userAuthorities;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public List<UserAuthority> getUserAuthorities() {
        return userAuthorities;
    }

    public void setUserAuthorities(List<UserAuthority> userAuthorities) {
        this.userAuthorities = userAuthorities;
    }

    public Set<SecurityBook> getSecurityBooks() {
        return securityBooks;
    }

    public void setSecurityBooks(Set<SecurityBook> securityBooks) {
        this.securityBooks = securityBooks;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "User{" +
                "authorization='" + login + '\'' +
                ", password='" + password + '\'' +
                ", profile=" + profile +
                ", userAuthorities=" + userAuthorities +
                ", securityBooks=" + securityBooks +
                '}';
    }
}
