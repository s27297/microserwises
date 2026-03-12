package api.events.Repositories;

import api.events.Entities.Wydarzenie;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface WydarzenieRepository extends MongoRepository<Wydarzenie, String> {

    Optional<Wydarzenie> findByNazwa(String nazwa);

}