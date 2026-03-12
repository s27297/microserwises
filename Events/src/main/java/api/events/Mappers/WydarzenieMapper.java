package api.events.Mappers;

import api.events.DTOs.Event.CreateWydarzenieRequest;
import api.events.DTOs.Event.WydarzenieDto;
import api.events.Entities.Wydarzenie;


public class WydarzenieMapper {

    public static WydarzenieDto toDto(Wydarzenie entity) {
        if (entity == null) return null;


        return new WydarzenieDto(
                entity.getId(),
                entity.getNazwa(),
                entity.getDataWyjazdu(),
                entity.getDataZakonczenia(),
                entity.getOpis(),
                entity.getOrganizatorId()
        );
    }

    // =======================
    // CREATE REQUEST -> ENTITY
    // =======================
    public static Wydarzenie fromCreateRequest(CreateWydarzenieRequest request) {
        if (request == null) return null;

        return WydarzenieDto.fromCreateRequest(request);
    }

}
