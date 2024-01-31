package br.com.ibmwallet.dtos;

import br.com.ibmwallet.entities.MoneyTransaction;

import java.util.Date;

public record MoneyTransactionFormattedDTO(Long id, Double value, String category_name, String recipient_name, Date date) {
    public MoneyTransactionFormattedDTO(MoneyTransaction data) {
        this(data.getId(), data.getValue(), data.getCategory().getName(), data.getRecipient().getName(), data.getCreatedAt());
    }
}
