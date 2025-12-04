package org.example.spothofra.repository;

import org.example.spothofra.entity.Role;
import org.example.spothofra.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRole(RoleName roleName);

    boolean existsByRole(RoleName roleName);
}
