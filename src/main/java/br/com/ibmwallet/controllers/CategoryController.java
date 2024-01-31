package br.com.ibmwallet.controllers;

import br.com.ibmwallet.dtos.CategoryDTO;
import br.com.ibmwallet.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("category")
@CrossOrigin("http://localhost:4200/")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<CategoryDTO> getAll() {
        return categoryService.getAll();
    }

    @PostMapping
    public ResponseEntity<String> save(@RequestBody CategoryDTO data) {
        return categoryService.save(data);
    }

    @PutMapping
    public ResponseEntity<String> update(@RequestParam("update") Long id, @RequestBody CategoryDTO data) { return categoryService.update(id, data); }

    @DeleteMapping
    public ResponseEntity<String> delete(@RequestParam("delete") Long id) { return categoryService.delete(id); }
}
