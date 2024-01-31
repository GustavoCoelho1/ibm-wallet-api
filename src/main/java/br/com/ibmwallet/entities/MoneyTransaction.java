package br.com.ibmwallet.entities;

import br.com.ibmwallet.dtos.MoneyTransactionDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name="moneyTransaction")
@Entity(name="moneyTransaction")
public class MoneyTransaction extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, columnDefinition="Decimal(10,2)")
    private Double value;

    @ManyToOne
    @JoinColumn(name="category_id", nullable=false)
    private Category category;

    @ManyToOne
    @JoinColumn(name="recipient_id", nullable=false)
    private Recipient recipient;

    @ManyToOne
    @JoinColumn(name="client_id", nullable=false)
    private Client client;

    public MoneyTransaction(MoneyTransactionDTO data) {
        this.value = data.value();
    }

    public Long getCategoryId() {
        return (category != null) ? category.getId() : null;
    }

    public Long getRecipientId() {
        return (recipient != null) ? recipient.getId() : null;
    }

    public Long getClientId() {
        return (client != null) ? client.getId() : null;
    }
}
