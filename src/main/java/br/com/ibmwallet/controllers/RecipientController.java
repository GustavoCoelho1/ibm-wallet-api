package br.com.ibmwallet.controllers;

import br.com.ibmwallet.dtos.RecipientDTO;
import br.com.ibmwallet.services.RecipientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("recipient")
@CrossOrigin("http://localhost:4200/")
public class RecipientController {
    @Autowired
    private RecipientService recipientService;

    @GetMapping
    public List<RecipientDTO> getAll() {
        return recipientService.getAll();
    }

    @PostMapping
    public ResponseEntity<String> save(@RequestBody RecipientDTO data) {
        return recipientService.save(data);
    }

    @PutMapping
    public ResponseEntity<String> update(@RequestParam("update") Long id, @RequestBody RecipientDTO data) { return recipientService.update(id, data); }

    @DeleteMapping
    public ResponseEntity<String> delete(@RequestParam("delete") Long id) { return recipientService.delete(id); }
}
