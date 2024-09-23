package org.ex.back.global.jwt;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlacklistRepository extends JpaRepository<BlacklistEntity, Integer> {

    Optional<BlacklistEntity> findByToken(String token);
}
