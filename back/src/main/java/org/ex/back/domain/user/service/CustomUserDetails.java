package org.ex.back.domain.user.service;

import lombok.Builder;
import org.ex.back.domain.owner.dto.TokenResponseDto;
import org.ex.back.domain.user.model.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Builder
public class CustomUserDetails implements UserDetails, OAuth2User {

    private UserEntity userEntity;
    private Map<String, Object> attributes;

    public CustomUserDetails(
            UserEntity userEntity,
            Map<String, Object> attributes
    ) {
        this.userEntity = userEntity;
        this.attributes = attributes;
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
