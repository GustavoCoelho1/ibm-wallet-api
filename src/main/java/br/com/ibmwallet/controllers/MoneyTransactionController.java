package br.com.ibmwallet.controllers;

import br.com.ibmwallet.dtos.MoneyTransactionDTO;
import br.com.ibmwallet.dtos.MoneyTransactionFormattedDTO;
import br.com.ibmwallet.services.MoneyTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("moneyTransaction")
@CrossOrigin("http://localhost:4200/")
public class MoneyTransactionController {
    @Autowired
    private MoneyTransactionService moneyTransactionService;

    @GetMapping
    public List<MoneyTransactionDTO> getAll() {
        return moneyTransactionService.getAll();
    }

    @GetMapping("/client")
    public List<MoneyTransactionDTO> getAllByClientIdFormatted(@RequestParam("id") Long id) {
        return moneyTransactionService.getAllByUserId(id);
    }

    @PostMapping
    public ResponseEntity<String> save(@RequestBody MoneyTransactionDTO data) {
        return moneyTransactionService.save(data);
    }

    @PutMapping
    public ResponseEntity<String> update(@RequestParam("update") Long id, @RequestBody MoneyTransactionDTO data) { return moneyTransactionService.update(id, data); }

    @DeleteMapping
    public ResponseEntity<String> delete(@RequestParam("delete") Long id) { return moneyTransactionService.delete(id); }
}