package br.com.ibmwallet.controllers;

import br.com.ibmwallet.dtos.ClientDTO;
import br.com.ibmwallet.dtos.LoginRequestDTO;
import br.com.ibmwallet.dtos.LoginResponseDTO;
import br.com.ibmwallet.entities.Client;
import br.com.ibmwallet.services.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("client")
@CrossOrigin(origins = {"http://localhost:4200", "https://ibm-wallet.vercel.app/"})
public class ClientController {
    @Autowired
    private ClientService clientService;

    @GetMapping
    @Operation(summary = "Resgata todos registros de Cliente, sem restrições", method = "GET")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Dados resgatados com sucesso"), @ApiResponse(responseCode = "500", description = "Erro ao resgatar dados") })
    public List<ClientDTO> getAll() {
        return clientService.getAll();
    }

    @PostMapping
    @Operation(summary = "Salva um novo registro de Cliente, criptografando sua senha", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados salvos com sucesso!"),
            @ApiResponse(responseCode = "500", description = "Erro ao salvar dados"),
            @ApiResponse(responseCode = "409", description = "Tentando inserir um e-mail já cadastrado"),
            @ApiResponse(responseCode = "400", description = "Erro nos dados passados para requisição")
    })
    public ResponseEntity<String> save(@RequestBody ClientDTO data) {
        return clientService.save(data);
    }

    @PostMapping("/login")
    @Operation(summary = "Autentica e retorna um token JWT contendo informações do Cliente, caso os dados sejam válidos", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente autenticado com sucesso!"),
            @ApiResponse(responseCode = "500", description = "Erro ao gerar token para Cliente"),
            @ApiResponse(responseCode = "400", description = "Dados de e-mail e/ou senha inválidos")
    })
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO data) {
        return clientService.login(data);
    }

    @PutMapping
    @Operation(summary = "Atualiza um registro de Cliente, cujo ID deve estar contido nos parâmetros da requisição", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados atualizados com sucesso!"),
            @ApiResponse(responseCode = "500", description = "Erro ao atualizar dados"),
            @ApiResponse(responseCode = "400", description = "Erro nos dados passados para requisição")
    })
    public ResponseEntity<String> update(@RequestParam("update") Long id, @RequestBody ClientDTO data) { return clientService.update(id, data); }

    @DeleteMapping
    @Operation(summary = "Deleta um registro de Cliente, cujo ID deve estar contido nos parâmetros da requisição", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados deletados com sucesso!"),
            @ApiResponse(responseCode = "500", description = "Erro ao deletar dados"),
            @ApiResponse(responseCode = "400", description = "Erro ao passar ID inválido (não cadastrado) para requisição")
    })
    public ResponseEntity<String> delete(@RequestParam("delete") Long id) { return clientService.delete(id); }
}