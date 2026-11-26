package vn.iotstar.service;

import java.util.Optional;

import org.springframework.data.domain.Page;

import vn.iotstar.entity.User;

public interface UserService {

    User register(User user);

    Optional<User> login(String username, String password);

    Page<User> getAllUsers(int page, int size, String keyword);

    Optional<User> getUserById(Integer id);

    Optional<User> getUserByUsername(String username);

    User createUser(User user);

    User updateUser(Integer id, User userDetails);

    boolean deleteUser(Integer id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    long count();
}
