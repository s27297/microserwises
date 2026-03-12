package com.pjatk.users.application;

import com.pjatk.users.core.port.in.commands.UserCommand;
import com.pjatk.users.core.port.out.UserWritePort;
import com.pjatk.users.core.domain.User;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class UserCommandService implements UserCommand {
    private final UserWritePort writePort;


    @Override
    public String save(User user) {
        return writePort.save(user);
    }

    @Override
    public User update(String id, User user) {
        return writePort.update(id,user);
    }

    @Override
    public boolean delete(String id) {
       return writePort.deleteById(id);
    }

    @Override
    public String addSprawnosc(String id, User.Sprawnosc sprawnosc) {
        return writePort.addSprawnosc(id,sprawnosc);
    }

    @Override
    public String deleteSprawnosc(String id, Integer source_id) {
        return writePort.deleteSprawnosc(id,source_id);
    }

    @Override
    public String addPayment(String id, User.Payment payment) {
        return writePort.addPayment(id,payment);
    }

    @Override
    public String deletePayment(String id, Integer source_id) {
        return writePort.deletePayment(id,source_id);
    }

    @Override
    public String updatePayment(String id, User.Payment payment) {
        return writePort.updatePayment(id,payment);
    }

    @Override
    public String updateSprawnosc(String id, User.Sprawnosc sprawnosc) {
        return writePort.updateSprawnosc(id,sprawnosc);
    }
}
