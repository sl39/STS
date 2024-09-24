package org.ex.back.domain.owner.service;

import lombok.RequiredArgsConstructor;
import org.ex.back.domain.owner.model.OwnerEntity;
import org.ex.back.domain.owner.repository.OwnerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class CustomOwnerDetailsService implements UserDetailsService {

    private final OwnerRepository ownerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public UserDetails loadUserByPk(Integer userPk) {
        OwnerEntity owner = ownerRepository.findById(userPk).orElseThrow(
                () -> new UsernameNotFoundException("id에 해당하는 Owner 없음")
        );

        return OwnerPrincipal.create(owner);
    }
}
