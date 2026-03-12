package com.pjatk.users.application;

import com.pjatk.users.core.port.in.queries.UsersQuery;
import com.pjatk.users.core.port.out.UsersReadPort;
import com.pjatk.users.core.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UsersQueryService implements UsersQuery {
    private final UsersReadPort usersReadPort;


    @Override
    public List<User> getUsers(Pageable pageable) {
        return usersReadPort.getUsers(pageable);
    }

    @Override
    public List<User> getUsersLogins(Pageable pageable) {
        return usersReadPort.getUsersLogins(pageable);

    }

    @Override
    public List<User> findByLoginPrefix(String prefix, Pageable pageable) {
        List<User> users = usersReadPort.findByLoginPrefix(prefix, pageable);
        return usersReadPort.findByLoginPrefix(prefix, pageable);
    }

    @Override
    public Optional<User> getUserById(String id) {
        return usersReadPort.getUserById(id);
    }
}
