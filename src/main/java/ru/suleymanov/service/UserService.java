package ru.suleymanov.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.suleymanov.entity.User;
import ru.suleymanov.entity.UserRole;
import ru.suleymanov.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> findAllByUserRoleIn(Iterable<UserRole> roles) {
        return userRepository.findAllByUserRoleInOrderByIdAsc(roles);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository
                .findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalStateException("Такой " + email + " не найден!"));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void updateUserRole(long id, UserRole role) {
        userRepository.updateUserRole(id, role);
    }
}
