package com.pjatk.users.adapters.web.contract.users;

import com.pjatk.users.core.domain.User;

public record SprawnoscDto(Integer source_id,String sprawnosc_type,String sprawnosc_name) {
    public static SprawnoscDto from(User.Sprawnosc sprawnosc){
        return new SprawnoscDto(
                sprawnosc.source_id(), sprawnosc.sprawnosc_type(), sprawnosc.sprawnosc_name()
        );
    }

    public static User.Sprawnosc to(SprawnoscDto sprawnosc){
        return new User.Sprawnosc(
                sprawnosc.source_id(), sprawnosc.sprawnosc_type(), sprawnosc.sprawnosc_name()
        );
    }
}
