package br.com.ibmwallet.controllers;

import br.com.ibmwallet.dtos.CategoryDTO;
import br.com.ibmwallet.dtos.RecipientDTO;
import br.com.ibmwallet.services.RecipientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("recipient")
@CrossOrigin(origins = {"http://localhost:4200", "https://ibm-wallet.vercel.app/"})
public class RecipientController {
    @Autowired
    private RecipientService recipientService;

    @GetMapping
    @Operation(summary = "Resgata todos registros de Destinatário, sem restrições", method = "GET")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Dados resgatados com sucesso"), @ApiResponse(responseCode = "500", description = "Erro ao resgatar dados") })
    public List<RecipientDTO> getAll() {
        return recipientService.getAll();
    }

    @GetMapping("/client")
    @Operation(summary = "Resgata todos registros de Destinatário emitidos por um Cliente, cujo ID deve estar contido nos parâmetros da requisição", method = "GET")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Dados resgatados com sucesso"), @ApiResponse(responseCode = "500", description = "Erro ao resgatar dados") })
    public List<RecipientDTO> getAllByClientId(@RequestParam("id") Long id) {
        return recipientService.getAllByClientId(id);
    }

    @PostMapping
    @Operation(summary = "Salva um novo registro de Destinatário", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados salvos com sucesso!"),
            @ApiResponse(responseCode = "500", description = "Erro ao salvar dados"),
            @ApiResponse(responseCode = "409", description = "Tentando inserir um nome já cadastrado"),
            @ApiResponse(responseCode = "400", description = "Erro nos dados passados para requisição")
    })
    public ResponseEntity<String> save(@RequestBody RecipientDTO data) {
        return recipientService.save(data);
    }

    @PutMapping
    @Operation(summary = "Atualiza um registro de Destinatário, cujo ID deve estar contido nos parâmetros da requisição", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados atualizados com sucesso!"),
            @ApiResponse(responseCode = "500", description = "Erro ao atualizar dados"),
            @ApiResponse(responseCode = "400", description = "Erro nos dados passados para requisição")
    })
    public ResponseEntity<String> update(@RequestParam("update") Long id, @RequestBody RecipientDTO data) { return recipientService.update(id, data); }

    @DeleteMapping
    @Operation(summary = "Deleta um registro de Destinatário, cujo ID deve estar contido nos parâmetros da requisição", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados deletados com sucesso!"),
            @ApiResponse(responseCode = "500", description = "Erro ao deletar dados"),
            @ApiResponse(responseCode = "400", description = "Erro ao passar ID inválido (não cadastrado) para requisição")
    })
    public ResponseEntity<String> delete(@RequestParam("delete") Long id) { return recipientService.delete(id); }
}
