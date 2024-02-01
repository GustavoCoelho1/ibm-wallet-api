package br.com.ibmwallet.dtos;

import br.com.ibmwallet.entities.Category;

public record CategoryDTO(Long id, String name, Long client_id) {
    public CategoryDTO(Category data) {
        this(data.getId(), data.getName(), data.getClientId());
    }
}
