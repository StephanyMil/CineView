package cs2024.inf.CineView.controllers;

import cs2024.inf.CineView.dto.CurtidaDto;
import cs2024.inf.CineView.models.CurtidaModel;
import cs2024.inf.CineView.services.CurtidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CurtidaController {

    @Autowired
    private CurtidaService curtidaService;

    @PostMapping("/{id_review}/curtidas")
    public ResponseEntity<CurtidaModel> createCurtida(@PathVariable Long id_review, @RequestBody CurtidaDto curtidaDto) {
        CurtidaModel curtida = curtidaService.saveCurtida(id_review, curtidaDto);
        if (curtida != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(curtida);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @DeleteMapping("/curtidas/{id}")
    public ResponseEntity<String> deleteCurtida(@PathVariable Long id) {
        curtidaService.deleteCurtida(id);
        return ResponseEntity.status(HttpStatus.OK).body("Curtida deleted successfully");
    }
}
