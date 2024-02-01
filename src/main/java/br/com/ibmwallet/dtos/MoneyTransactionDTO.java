package br.com.ibmwallet.dtos;

import br.com.ibmwallet.entities.Category;
import br.com.ibmwallet.entities.MoneyTransaction;
import br.com.ibmwallet.entities.Recipient;

import java.time.LocalDate;
import java.util.Date;

public record MoneyTransactionDTO(Long id, Double value, Long category_id, Long recipient_id, Long client_id, LocalDate date) {
    public MoneyTransactionDTO(MoneyTransaction data) {
        this(data.getId(), data.getValue(), data.getCategoryId(), data.getRecipientId(), data.getClientId(), data.getDate());
    }
}
