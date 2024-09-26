package org.ex.back.domain.user.service;

import lombok.Builder;
import org.ex.back.domain.owner.model.OwnerEntity;
import org.ex.back.domain.owner.service.OwnerPrincipal;
import org.ex.back.domain.user.model.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@Builder
public class UserPrincipal implements UserDetails, OAuth2User {

    private UserEntity userEntity;
    private Map<String, Object> attributes;

    public UserPrincipal(
            UserEntity userEntity,
            Map<String, Object> attributes
    ) {
        this.userEntity = userEntity;
        this.attributes = attributes;
    }

    public static UserPrincipal create(UserEntity user) {

        return new UserPrincipal(user, new HashMap<>());
    }

    public Integer getUserPk() {
        return userEntity.getUser_pk();
    }

    // UserDetails method override
    @Override
    public List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return userEntity.getUser_pk().toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // OAuth2User method override
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return userEntity.getUser_pk().toString();
    }
}
