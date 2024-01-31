package br.com.ibmwallet.services;

import br.com.ibmwallet.dtos.CategoryDTO;
import br.com.ibmwallet.entities.Category;
import br.com.ibmwallet.repositories.CategoryRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryDTO> getAll() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDTO> response = new ArrayList<>();

        for (Category category:categories) {
            response.add(new CategoryDTO(category));
        }

        return response;
    }

    public ResponseEntity<String> save(CategoryDTO data) {
        System.out.print(categoryRepository.findByName(data.name()));

        if(categoryRepository.findByName(data.name()).isEmpty()) {
            if (data.name() == null) {
                return new ResponseEntity<>("Todos os dados são necessários para cadastrar o registro!", HttpStatus.BAD_REQUEST);
            }

            Category category = new Category(data);
            Category newCategory = categoryRepository.save(category);

            if (newCategory.getId() != null) {
                return new ResponseEntity<>("Registro cadastrado com sucesso!", HttpStatus.OK);
            }
            return new ResponseEntity<>("Houve um erro ao cadastrar o registro!", HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>("Registro já cadastrado!", HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<String> update(Long id, CategoryDTO data) {
        Optional<Category> category = categoryRepository.findById(id);

        if (category.isPresent() && data.name() != null) {
            Category newCategory = new Category(data);
            newCategory.setId(category.get().getId());
            newCategory.setName(data.name());

            Category updatedCategory = categoryRepository.save(newCategory);

            if (updatedCategory.getId() != null) {
                return new ResponseEntity<>("Registro atualizado com sucesso!", HttpStatus.OK);
            }
            return new ResponseEntity<>("Houve um erro ao atualizar o registro!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
    }

    public ResponseEntity<String> delete(Long id) {
        Optional<Category> category = categoryRepository.findById(id);

        if (category.isPresent()) {
            categoryRepository.delete(category.get());

            if (categoryRepository.findById(id).isEmpty()){
                return new ResponseEntity<>("Registro apagado com sucesso!", HttpStatus.OK);
            }
            return new ResponseEntity<>("Houve um erro ao apagar o registro!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("ID inexistente!", HttpStatus.BAD_REQUEST);
    }
}
