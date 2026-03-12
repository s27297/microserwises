package com.pjatk.users.core.port.in.commands;

import com.pjatk.users.core.domain.User;

public interface UserCommand {
     String save(User user);
     User update(String id,User user);
     boolean delete(String id);
     String addSprawnosc(String id,User.Sprawnosc sprawnosc);
     String deleteSprawnosc(String id,Integer source_id);
     String addPayment(String id,User.Payment payment);
     String deletePayment(String id,Integer source_id);
     String updatePayment(String id, User.Payment payment);
     String updateSprawnosc(String id, User.Sprawnosc sprawnosc);
}
