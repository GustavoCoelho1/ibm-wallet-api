package br.com.ibmwallet.dtos;

import br.com.ibmwallet.entities.MoneyTransaction;

import java.util.Date;

public record MoneyTransactionDTO(Long id, Double value, Long category_id, Long recipient_id, Long client_id, Date created_at, Date updated_at) {
    public MoneyTransactionDTO(MoneyTransaction data) {
        this(data.getId(), data.getValue(), data.getCategoryId(), data.getRecipientId(), data.getClientId(), data.getCreatedAt(), data.getUpdatedAt());
    }
}
