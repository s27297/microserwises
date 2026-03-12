package api.events.DTOs.Event;


import api.events.Entities.Wydarzenie;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WydarzenieDto {
    private String id;
    private String nazwa;
    private LocalDateTime dataWyjazdu;
    private LocalDateTime dataZakonczenia;
    private String opis;
    private String organizatorId;

    public static Wydarzenie fromCreateRequest(CreateWydarzenieRequest request) {
        Wydarzenie w = new Wydarzenie();
        w.setNazwa(request.getNazwa());
        w.setDataWyjazdu(request.getDataWyjazdu());
        w.setDataZakonczenia(request.getDataZakonczenia());
        w.setOpis(request.getOpis());
        return w;
    }

}
