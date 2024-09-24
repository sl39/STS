package org.ex.back.global.jwt;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    Optional<RefreshTokenEntity> findByToken(String token);

    Optional<RefreshTokenEntity> findByOwnerPk(Integer ownerPk);

    Optional<RefreshTokenEntity> findByUserPk(Integer userPk);
}
