package com.pjatk.users.adapters.web.contract.users;

import com.pjatk.users.core.domain.User;

import java.util.Date;

public record PaymentDto(Integer source_id, String nazwa, Date date, Double quantity)  {
    public static PaymentDto from(User.Payment payment){
        return new PaymentDto(
                payment.source_id(), payment.nazwa(),payment.date(),payment.quantity()
        );
    }

    public static User.Payment to(PaymentDto payment){
        return new User.Payment(
                payment.source_id(), payment.nazwa(),payment.date(),payment.quantity()
        );
    }
}
