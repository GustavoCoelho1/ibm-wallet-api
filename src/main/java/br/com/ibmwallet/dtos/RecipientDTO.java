package br.com.ibmwallet.dtos;

import br.com.ibmwallet.entities.Recipient;

public record RecipientDTO(Long id, String name, Long client_id) {
    public RecipientDTO(Recipient data) {
        this(data.getId(), data.getName(), data.getClientId());
    }
}
