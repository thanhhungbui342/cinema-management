package com.thanhhungbui342.cinema.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thanhhungbui342.cinema.entity.Role;
import com.thanhhungbui342.cinema.entity.Role.RoleName;

public interface RoleRepository extends JpaRepository<Role, Long>{

    Optional<Role> findByName(RoleName user);
}
