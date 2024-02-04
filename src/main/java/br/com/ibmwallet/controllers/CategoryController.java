package br.com.ibmwallet.controllers;

import br.com.ibmwallet.dtos.CategoryDTO;
import br.com.ibmwallet.dtos.MoneyTransactionDTO;
import br.com.ibmwallet.services.CategoryService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://ibm-wallet.vercel.app/"})
@RequestMapping("category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Resgata todos registros de Categoria, sem restrições", method = "GET")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Dados resgatados com sucesso"), @ApiResponse(responseCode = "500", description = "Erro ao resgatar dados") })
    public List<CategoryDTO> getAll() {
        return categoryService.getAll();
    }

    @GetMapping("/client")
    @Operation(summary = "Resgata todos registros de Categoria emitidos por um Cliente, cujo ID deve estar contido nos parâmetros da requisição", method = "GET")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Dados resgatados com sucesso!"), @ApiResponse(responseCode = "500", description = "Erro ao resgatar dados!") })
    public List<CategoryDTO> getAllByClientId(@RequestParam("id") Long id) {
        return categoryService.getAllByClientId(id);
    }

    @PostMapping
    @Operation(summary = "Salva um novo registro de Categoria", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados salvos com sucesso!"),
            @ApiResponse(responseCode = "500", description = "Erro ao salvar dados"),
            @ApiResponse(responseCode = "409", description = "Tentando inserir um nome já cadastrado"),
            @ApiResponse(responseCode = "400", description = "Erro nos dados passados para requisição")
    })
    public ResponseEntity<String> save(@RequestBody CategoryDTO data) {
        return categoryService.save(data);
    }

    @PutMapping
    @Operation(summary = "Atualiza um registro de Categoria, cujo ID deve estar contido nos parâmetros da requisição", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados atualizados com sucesso!"),
            @ApiResponse(responseCode = "500", description = "Erro ao atualizar dados"),
            @ApiResponse(responseCode = "400", description = "Erro nos dados passados para requisição")
    })
    public ResponseEntity<String> update(@RequestParam("update") Long id, @RequestBody CategoryDTO data) { return categoryService.update(id, data); }

    @DeleteMapping
    @Operation(summary = "Deleta um registro de Categoria, cujo ID deve estar contido nos parâmetros da requisição", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados deletados com sucesso!"),
            @ApiResponse(responseCode = "500", description = "Erro ao deletar dados"),
            @ApiResponse(responseCode = "400", description = "Erro ao passar ID inválido (não cadastrado) para requisição")
    })
    public ResponseEntity<String> delete(@RequestParam("delete") Long id) { return categoryService.delete(id); }
}
