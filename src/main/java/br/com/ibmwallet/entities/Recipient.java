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
@Table(name="recipient")
@Entity(name="recipient")
public class Recipient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique=true, nullable=false)
    private String name;

    @OneToMany(mappedBy="recipient")
    private List<MoneyTransaction> transactions;

    public Recipient(RecipientDTO data) {
        this.name = data.name();
    }
}
