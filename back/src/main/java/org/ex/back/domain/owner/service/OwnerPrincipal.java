package org.ex.back.domain.owner.service;

import lombok.Data;
import org.ex.back.domain.owner.model.OwnerEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
public class OwnerPrincipal implements UserDetails {

    private Integer pk;
    private String email;
    private Collection<? extends GrantedAuthority> authorities;

    public OwnerPrincipal(Integer pk, String email, Collection<? extends GrantedAuthority> authorities) {
        this.pk = pk;
        this.email = email;
        this.authorities = authorities;
    }

    public static OwnerPrincipal create(OwnerEntity owner) {
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_OWNER"));

        return new OwnerPrincipal(owner.getOwner_pk(), owner.getEmail(), authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
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
}
