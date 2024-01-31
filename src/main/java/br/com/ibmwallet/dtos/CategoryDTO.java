package br.com.ibmwallet.dtos;

import br.com.ibmwallet.entities.Category;

public record CategoryDTO(Long id, String name) {
    public CategoryDTO(Category data) {
        this(data.getId(), data.getName());
    }
}
