package br.com.ibmwallet.controllers;

import br.com.ibmwallet.dtos.LargeScaleSaveRequestDTO;
import br.com.ibmwallet.dtos.MoneyTransactionDTO;
import br.com.ibmwallet.services.MoneyTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Resgata todos registros de Transação, sem restrições", method = "GET")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Dados resgatados com sucesso"), @ApiResponse(responseCode = "500", description = "Erro ao resgatar dados") })
    public List<MoneyTransactionDTO> getAll() {
        return moneyTransactionService.getAll();
    }

    @GetMapping("/client")
    @Operation(summary = "Resgata todos registros de Transação emitidos por um Cliente, cujo ID deve estar contido nos parâmetros da requisição", method = "GET")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Dados resgatados com sucesso!"), @ApiResponse(responseCode = "500", description = "Erro ao resgatar dados!") })
    public List<MoneyTransactionDTO> getAllByClientId(@RequestParam("id") Long id) {
        return moneyTransactionService.getAllByClientId(id);
    }

    @PostMapping
    @Operation(summary = "Salva um novo registro de Transação", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados salvos com sucesso!"),
            @ApiResponse(responseCode = "500", description = "Erro ao salvar dados"),
            @ApiResponse(responseCode = "400", description = "Erro nos dados passados para requisição")
    })
    public ResponseEntity<String> save(@RequestBody MoneyTransactionDTO data) {
        return moneyTransactionService.save(data);
    }

    @PostMapping("/largeScale")
    @Operation(summary = "Salva múltiplos registros de Transação de uma só vez. Template ('<data>,<valor>,<nome_categoria>,<destinátário/remetente>')", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados salvos com sucesso!"),
            @ApiResponse(responseCode = "500", description = "Erro ao salvar dados"),
            @ApiResponse(responseCode = "400", description = "Erro nos dados passados para requisição")
    })
    public ResponseEntity<String> largeScaleSave(@RequestBody LargeScaleSaveRequestDTO data) {
        return moneyTransactionService.largeScaleSave(data);
    }

    @PutMapping
    @Operation(summary = "Atualiza um registro de Transação, cujo ID deve estar contido nos parâmetros da requisição", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados atualizados com sucesso!"),
            @ApiResponse(responseCode = "500", description = "Erro ao atualizar dados"),
            @ApiResponse(responseCode = "400", description = "Erro nos dados passados para requisição")
    })
    public ResponseEntity<String> update(@RequestParam("update") Long id, @RequestBody MoneyTransactionDTO data) { return moneyTransactionService.update(id, data); }

    @DeleteMapping
    @Operation(summary = "Deleta um registro de Transação, cujo ID deve estar contido nos parâmetros da requisição", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados deletados com sucesso!"),
            @ApiResponse(responseCode = "500", description = "Erro ao deletar dados"),
            @ApiResponse(responseCode = "400", description = "Erro ao passar ID inválido (não cadastrado) para requisição")
    })
    public ResponseEntity<String> delete(@RequestParam("delete") Long id) { return moneyTransactionService.delete(id); }

    @DeleteMapping("/clearAll")
    @Operation(summary = "Limpa todos os registros da tabela de Transação, sem restrições", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados deletados com sucesso!"),
            @ApiResponse(responseCode = "500", description = "Erro ao deletar dados"),
    })
    public ResponseEntity<String> clearAll() { return moneyTransactionService.clearAll(); }
}