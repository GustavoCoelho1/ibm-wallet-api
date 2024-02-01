package br.com.ibmwallet.repositories;

import br.com.ibmwallet.entities.Recipient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipientRepository extends JpaRepository<Recipient, Long> {
    List<Recipient> findByName(String name);

    @Query(value = "SELECT * FROM recipient WHERE client_id = :id", nativeQuery = true)
    List<Recipient> findAllByClientId(@Param("id") Long id);
}
