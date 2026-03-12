package api.events.Controllers;

import api.events.DTOs.Event.CreateWydarzenieRequest;
import api.events.DTOs.Event.UpdateWydarzenieRequest;
import api.events.DTOs.Event.WydarzenieDto;
import api.events.Entities.Wydarzenie;
import api.events.Mappers.WydarzenieMapper;
import api.events.Services.WydarzenieService;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Collectors;

import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/wydarzenie")
public class WydarzenieController {

    private final WydarzenieService wydarzenieService;

    public WydarzenieController(WydarzenieService wydarzenieService) {
        this.wydarzenieService = wydarzenieService;
    }

    @GetMapping("/debug")
    public String debugToken(@AuthenticationPrincipal Jwt jwt) {
        System.out.println("JWT Token: " + jwt.getTokenValue());
        jwt.getClaims().forEach((k, v) -> System.out.println(k + " -> " + v));
        return "Check logs for token claims";
    }

    @GetMapping("/api/me")
    public String whoAmI(@AuthenticationPrincipal Jwt jwt) {
        return "Logged in user: " + jwt.getClaimAsString("preferred_username");
    }

        @PostMapping("/pdf")
        public ResponseEntity<byte[]> createWydarzeniePdf(
                @RequestBody CreateWydarzenieRequest request) {

            // Tworzymy wydarzenie
            Wydarzenie saved = wydarzenieService.createWydarzenie(request);
            WydarzenieDto dto = WydarzenieMapper.toDto(saved);

            // Generujemy PDF
            byte[] pdfBytes = generatePdf(dto);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.set(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=wydarzenie_" + dto.getId() + ".pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        }

        private byte[] generatePdf(WydarzenieDto dto) {
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

                PdfWriter writer = new PdfWriter(baos);
                PdfDocument pdfDoc = new PdfDocument(writer);
                Document document = new Document(pdfDoc);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                document.add(new Paragraph("Szczegóły wydarzenia")
                        .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                        .setFontSize(16));

                document.add(new Paragraph("ID: " + dto.getId()));
                document.add(new Paragraph("Nazwa: " + dto.getNazwa()));
                document.add(new Paragraph("Data wyjazdu: " + dto.getDataWyjazdu().format(dtf)));
                document.add(new Paragraph("Data zakończenia: " + dto.getDataZakonczenia().format(dtf)));
                document.add(new Paragraph("Opis: " + dto.getOpis()));
                document.add(new Paragraph("Organizator ID: " + dto.getOrganizatorId()));

                document.close();
                return baos.toByteArray();
            } catch (Exception e) {
                throw new RuntimeException("Błąd generowania PDF", e);
            }
        }


    @PostMapping()
    public ResponseEntity<WydarzenieDto> createWydarzenie(
            @RequestBody CreateWydarzenieRequest createWydarzenieRequest)
            {

        Wydarzenie saved = wydarzenieService.createWydarzenie(createWydarzenieRequest);
        return ResponseEntity.ok(WydarzenieMapper.toDto(saved));
    }


    @GetMapping
    public ResponseEntity<List<WydarzenieDto>> getAll() {
        List<WydarzenieDto> list = wydarzenieService.getAllWydarzenia()
                .stream()
                .map(WydarzenieMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WydarzenieDto> getById(@PathVariable String id) {
        Wydarzenie w = wydarzenieService.getWydarzenieById(id);
        return ResponseEntity.ok(WydarzenieMapper.toDto(w));
    }

    @GetMapping("/nazwa/{nazwa}")
    public ResponseEntity<WydarzenieDto> getByNazwa(@PathVariable String nazwa) {
        Wydarzenie w = wydarzenieService.getWydarzenieByNazwa(nazwa);
        return ResponseEntity.ok(WydarzenieMapper.toDto(w));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<WydarzenieDto> updateWydarzenie(
            @PathVariable String id,
            @RequestBody UpdateWydarzenieRequest request) {

        Wydarzenie updated = wydarzenieService.modifyWydarzenie(id, request);
        return ResponseEntity.ok(WydarzenieMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        wydarzenieService.deleteWydarzenie(id);
        return ResponseEntity.noContent().build();
    }


}