package br.com.ibmwallet.entities;

import br.com.ibmwallet.dtos.RecipientDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Table(name="recipient", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "client_id"})}) //Essa constraint restringe um cliente a poder criar um destinatário/remetente apenas uma vez, sem poder repití-lo.
@Entity(name="recipient")
public class Recipient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false)
    private String name;

    @ManyToOne
    @JoinColumn(name="client_id", nullable=false)
    private Client client;

    @OneToMany(mappedBy="recipient")
    private List<MoneyTransaction> transactions;

    public Recipient(RecipientDTO data) {
        this.name = data.name();
    }

    public Long getClientId() {
        return (client != null) ? client.getId() : null;
    }
}
