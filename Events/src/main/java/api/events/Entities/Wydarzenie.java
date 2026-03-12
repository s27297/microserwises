package api.events.Entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "events")
@Getter
@Setter
public class Wydarzenie {

    @Id
    private String id;   // Mongo używa String / ObjectId

    private String nazwa;

    private LocalDateTime dataWyjazdu;

    private LocalDateTime dataZakonczenia;

    private String opis;

    private String organizatorId; // w Mongo raczej String, nie Long
}
