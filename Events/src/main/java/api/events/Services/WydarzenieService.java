package api.events.Services;

import api.events.DTOs.Event.CreateWydarzenieRequest;
import api.events.DTOs.Event.UpdateWydarzenieRequest;
import api.events.Entities.Wydarzenie;
import api.events.Mappers.WydarzenieMapper;
import api.events.Repositories.WydarzenieRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
public class WydarzenieService {

    private final WydarzenieRepository wydarzenieRepository;

    public WydarzenieService(WydarzenieRepository wydarzenieRepository)
                              {
        this.wydarzenieRepository = wydarzenieRepository;
    }



    public Wydarzenie createWydarzenie(CreateWydarzenieRequest request) {

        Wydarzenie wydarzenie = WydarzenieMapper.fromCreateRequest(request);


        wydarzenie.setOrganizatorId(request.getOrganizatorId());

        return wydarzenieRepository.save(wydarzenie);
    }



    public List<Wydarzenie> getAllWydarzenia() {
        return wydarzenieRepository.findAll();
    }

    public Wydarzenie getWydarzenieById(String id) {
        return wydarzenieRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Wydarzenie nie znalezione"));
    }


    public void deleteWydarzenie(String id) {
        wydarzenieRepository.deleteById(id);
    }

    public Wydarzenie modifyWydarzenie(String id, UpdateWydarzenieRequest request) {

        Wydarzenie wydarzenie = getWydarzenieById(id);

// EDYCJA TREŚCI
        wydarzenie.setNazwa(request.getNazwa());

        if (request.getDataWyjazdu() != null) {
            wydarzenie.setDataWyjazdu(
                    request.getDataWyjazdu()
            );
        }
        if (request.getDataZakonczenia() != null) {
            wydarzenie.setDataZakonczenia(
                    request.getDataZakonczenia()
            );
        }
        wydarzenie.setOpis(request.getOpis());
        // Ustawienie typu wydarzenia





        return wydarzenieRepository.save(wydarzenie);
    }


    public Wydarzenie getWydarzenieByNazwa(String nazwa) {
        return wydarzenieRepository.findByNazwa(nazwa)
                .orElseThrow(() -> new NoSuchElementException("Wydarzenie nie znalezione o nazwie: " + nazwa));
    }
    private void usunPlikZDisku(String sciezka) {
        System.out.println(sciezka);
        try {
            Files.deleteIfExists(Paths.get("uploads/wydarzenia/", sciezka));
        } catch (IOException e) {
            throw new RuntimeException("Nie udało się usunąć pliku: " + sciezka, e);
        }
    }



}
