package dev.elieweb.timeaway.auth.service;

import dev.elieweb.timeaway.auth.entity.User;
import dev.elieweb.timeaway.auth.repository.UserRepository;
import dev.elieweb.timeaway.common.exception.ResourceNotFoundException;
import dev.elieweb.timeaway.department.entity.Department;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrentUserService {
    private final UserRepository userRepository;

    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
    }

    public boolean isCurrentUser(User user) {
        return getCurrentUser().getId().equals(user.getId());
    }

    public boolean hasRole(String role) {
        return SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(role));
    }

    public List<User> findAllUsers(Sort sort) {
        return userRepository.findAll(sort);
    }

    public List<User> findUsersByDepartment(Department department) {
        return userRepository.findByDepartment(department);
    }
} 