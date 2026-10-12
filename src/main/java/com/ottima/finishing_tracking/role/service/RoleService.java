package com.ottima.finishing_tracking.role.service;

import com.ottima.finishing_tracking.common.messages.Messages;
import com.ottima.finishing_tracking.exception.RoleAlreadyExistsException;
import com.ottima.finishing_tracking.exception.RoleInUseException;
import com.ottima.finishing_tracking.exception.RoleNotFoundException;
import com.ottima.finishing_tracking.exception.SystemRoleModificationException;
import com.ottima.finishing_tracking.role.dto.CreateRoleRequest;
import com.ottima.finishing_tracking.role.dto.RoleResponse;
import com.ottima.finishing_tracking.role.entity.Role;
import com.ottima.finishing_tracking.role.mapper.RoleMapper;
import com.ottima.finishing_tracking.role.repository.RoleRepository;
import com.ottima.finishing_tracking.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class RoleService {
    private static final List<String> SYSTEM_ROLES = List.of("ADMIN", "ENGINEER", "CLIENT");

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final UserRepository userRepository;

    @Transactional
    @CacheEvict(value = "roles", allEntries = true)
    public RoleResponse createRole(@Valid CreateRoleRequest request){
        String normalizedRoleName = request.getRoleName().trim().toUpperCase();

        if(roleRepository.findByRoleName(normalizedRoleName).isPresent()){
            throw new RoleAlreadyExistsException();
        }
        Role role = roleMapper.toEntity(request);
        role.setRoleName(normalizedRoleName);
        Role savedRole = roleRepository.save(role);
        RoleResponse res = roleMapper.toResponse(savedRole);
        res.setUserCount(0);
        return res;
    }

    @Transactional
    @CacheEvict(value = "roles", allEntries = true)
    public RoleResponse updateRole(Long roleId, @Valid CreateRoleRequest request){
        Role role = roleRepository.findById(roleId)
                .orElseThrow(RoleNotFoundException::new);

        String currentRoleName = role.getRoleName().trim().toUpperCase();
        if (SYSTEM_ROLES.contains(currentRoleName)) {
            throw new SystemRoleModificationException(Messages.SYSTEM_ROLE_CANNOT_BE_MODIFIED);
        }

        long assignedUsers = userRepository.countByRole_RoleName(currentRoleName);
        if (assignedUsers > 0) {
            throw new RoleInUseException(Messages.ROLE_IN_USE_UPDATE);
        }

        String normalizedNewRoleName = request.getRoleName().trim().toUpperCase();
        if(roleRepository.findByRoleName(normalizedNewRoleName).isPresent() && !currentRoleName.equals(normalizedNewRoleName)){
            throw new RoleAlreadyExistsException();
        }

        role.setRoleName(normalizedNewRoleName);
        Role savedRole = roleRepository.save(role);
        RoleResponse res = roleMapper.toResponse(savedRole);
        res.setUserCount(0);
        return res;
    }

    public Page<RoleResponse> getAllRoles(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return roleRepository.findAll(pageable)
                .map(role -> {
                    RoleResponse res = roleMapper.toResponse(role);
                    res.setUserCount(userRepository.countByRole_RoleName(role.getRoleName()));
                    return res;
                });
    }

    @Cacheable(value = "roles", key = "'id-' + #roleId")
    public RoleResponse getRoleById(Long roleId){
        Role role = roleRepository.findById(roleId)
                .orElseThrow(RoleNotFoundException::new);
        RoleResponse res = roleMapper.toResponse(role);
        res.setUserCount(userRepository.countByRole_RoleName(role.getRoleName()));
        return res;
    }

    @Cacheable(value = "roles", key = "'name-' + #roleName.trim().toUpperCase()")
    public RoleResponse getRoleByName(String roleName){
        Role role = roleRepository.findByRoleName(roleName.trim().toUpperCase())
                .orElseThrow(RoleNotFoundException::new);
        RoleResponse res = roleMapper.toResponse(role);
        res.setUserCount(userRepository.countByRole_RoleName(role.getRoleName()));
        return res;
    }

    @CacheEvict(value = "roles", allEntries = true)
    @Transactional
    public void deleteRole(Long roleId){
        Role role = roleRepository.findById(roleId)
                .orElseThrow(RoleNotFoundException::new);

        String currentRoleName = role.getRoleName().trim().toUpperCase();
        if (SYSTEM_ROLES.contains(currentRoleName)) {
            throw new SystemRoleModificationException(Messages.SYSTEM_ROLE_CANNOT_BE_DELETED);
        }

        long assignedUsers = userRepository.countByRole_RoleName(currentRoleName);
        if (assignedUsers > 0) {
            throw new RoleInUseException(Messages.ROLE_IN_USE_DELETE);
        }

        roleRepository.delete(role);
    }
}

