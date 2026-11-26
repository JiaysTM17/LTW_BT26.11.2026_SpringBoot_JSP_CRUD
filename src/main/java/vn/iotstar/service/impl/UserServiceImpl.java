package vn.iotstar.service.impl;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.iotstar.entity.User;
import vn.iotstar.repository.UserRepository;
import vn.iotstar.service.UserService;
import vn.iotstar.util.PasswordUtil;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User register(User user) {
        user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
        if (user.getRoleid() == null) {
            user.setRoleid(1); // Standard User
        }
        if (user.getStatus() == null) {
            user.setStatus(1); // Active
        }
        if (user.getCreateddate() == null) {
            user.setCreateddate(new Date());
        }
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> login(String username, String rawPassword) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (PasswordUtil.checkPassword(rawPassword, user.getPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> getAllUsers(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(Math.max(0, page), size, Sort.by(Sort.Direction.DESC, "id"));
        if (keyword != null && !keyword.trim().isEmpty()) {
            String kw = keyword.trim();
            return userRepository.findByUsernameContainingIgnoreCaseOrFullnameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                    kw, kw, kw, pageable);
        }
        return userRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User createUser(User user) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
        }
        if (user.getCreateddate() == null) {
            user.setCreateddate(new Date());
        }
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Integer id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay nguoi dung voi ID: " + id));

        user.setFullname(userDetails.getFullname());
        user.setEmail(userDetails.getEmail());
        user.setPhone(userDetails.getPhone());
        user.setAvatar(userDetails.getAvatar());

        if (userDetails.getRoleid() != null) {
            user.setRoleid(userDetails.getRoleid());
        }
        if (userDetails.getStatus() != null) {
            user.setStatus(userDetails.getStatus());
        }

        // Only update password if a new one is provided
        if (userDetails.getPassword() != null && !userDetails.getPassword().trim().isEmpty()) {
            user.setPassword(PasswordUtil.hashPassword(userDetails.getPassword().trim()));
        }

        return userRepository.save(user);
    }

    @Override
    public boolean deleteUser(Integer id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return userRepository.count();
    }
}
