package br.com.ibmwallet.dtos;

import br.com.ibmwallet.entities.Client;
import br.com.ibmwallet.entities.MoneyTransaction;

import java.util.List;

public record ClientDTO(Long id, String name, String email, String password) {
    public ClientDTO(Client data) {
        this(data.getId(), data.getName(), data.getEmail(), data.getPassword());
    }
}
