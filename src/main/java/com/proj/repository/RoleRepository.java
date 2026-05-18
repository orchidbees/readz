package com.proj.repository;

import com.proj.constant.RoleType;
import com.proj.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role getRoleByType(RoleType name);
}
