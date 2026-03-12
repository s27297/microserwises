package com.pjatk.users.core.port.out;

import com.pjatk.users.core.domain.User;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UsersReadPort {
    List<User> getUsers(Pageable pageable);
    List<User> getUsersLogins(Pageable pageable);
    List<User> findByLoginPrefix(String prefix, Pageable pageable);
    Optional<User> getUserById(String id);

}
