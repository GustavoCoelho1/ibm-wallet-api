package br.com.ibmwallet.entities;

import br.com.ibmwallet.dtos.ClientDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

//Utilizando o nome "client" neste modulo porque "user" é uma palavra reservada no banco de dados.

@Getter
@Setter
@NoArgsConstructor
@Table(name="client")
@Entity(name="client")
@ToString
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy="client", cascade = CascadeType.REMOVE)
    private List<MoneyTransaction> transactions;

    public Client(ClientDTO data) {
        this.name = data.name();
        this.email = data.email();
        this.password = data.password();
    }
}
