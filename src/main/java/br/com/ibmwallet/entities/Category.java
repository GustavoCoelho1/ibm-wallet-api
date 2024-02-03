package br.com.ibmwallet.entities;

import br.com.ibmwallet.dtos.CategoryDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Table(name="category", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "client_id"})}) //Essa constraint restringe um cliente a poder criar uma categoria apenas uma vez, sem poder repití-la.
@Entity(name="category")

public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false)
    private String name;

    @ManyToOne
    @JoinColumn(name="client_id", nullable=false)
    private Client client;

    @OneToMany(mappedBy="category")
    private List<MoneyTransaction> transactions;

    public Category(CategoryDTO data) {
        this.name = data.name();
    }

    public Long getClientId() {
        return (client != null) ? client.getId() : null;
    }
}
