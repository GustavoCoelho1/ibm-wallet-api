package br.com.ibmwallet.dtos;

import br.com.ibmwallet.entities.Client;

public record ClientDTO(Long id, String name, String email, String password) {
    public ClientDTO(Client data) {
        this(data.getId(), data.getName(), data.getEmail(), data.getPassword());
    }
}
