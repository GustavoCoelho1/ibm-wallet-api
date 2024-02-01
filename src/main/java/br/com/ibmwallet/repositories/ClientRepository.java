package br.com.ibmwallet.repositories;

import br.com.ibmwallet.entities.Category;
import br.com.ibmwallet.entities.Client;
import br.com.ibmwallet.entities.MoneyTransaction;
import br.com.ibmwallet.entities.Recipient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findByName(String name);
    @Query(value = "SELECT * FROM client WHERE email = :email", nativeQuery = true)
    Client findByEmail(@Param("email") String email);
}
