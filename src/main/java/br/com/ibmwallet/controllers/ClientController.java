package br.com.ibmwallet.controllers;

import br.com.ibmwallet.dtos.ClientDTO;
import br.com.ibmwallet.dtos.LoginRequestDTO;
import br.com.ibmwallet.dtos.LoginResponseDTO;
import br.com.ibmwallet.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("client")
@CrossOrigin("http://localhost:4200/")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @GetMapping
    public List<ClientDTO> getAll() {
        return clientService.getAll();
    }

    @PostMapping
    public ResponseEntity<String> save(@RequestBody ClientDTO data) {
        return clientService.save(data);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO data) {
        return clientService.login(data);
    }

    @PutMapping
    public ResponseEntity<String> update(@RequestParam("update") Long id, @RequestBody ClientDTO data) { return clientService.update(id, data); }

    @DeleteMapping
    public ResponseEntity<String> delete(@RequestParam("delete") Long id) { return clientService.delete(id); }
}