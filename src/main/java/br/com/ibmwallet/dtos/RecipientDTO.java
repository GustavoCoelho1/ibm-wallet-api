package br.com.ibmwallet.dtos;

import br.com.ibmwallet.entities.Recipient;

public record RecipientDTO(Long id, String name) {
    public RecipientDTO(Recipient data) {
        this(data.getId(), data.getName());
    }
}
