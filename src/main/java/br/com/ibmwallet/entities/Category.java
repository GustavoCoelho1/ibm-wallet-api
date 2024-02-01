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
@Table(name="category")
@Entity(name="category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique=true, nullable=false)
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
