package br.com.ibmwallet.dtos;

import br.com.ibmwallet.entities.Client;

public record LoginResponseDTO (Long id, String name, String email) {
    public LoginResponseDTO(Client data) {
        this(data.getId(),data.getName(),data.getEmail());
    }
}
