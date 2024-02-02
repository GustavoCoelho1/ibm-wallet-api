package br.com.ibmwallet.repositories;

import br.com.ibmwallet.entities.MoneyTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MoneyTransactionRepository extends JpaRepository<MoneyTransaction, Long> {
    @Query(value = "SELECT * FROM money_transaction WHERE client_id = :id", nativeQuery = true)
    List<MoneyTransaction> findAllByClientId(@Param("id") Long id);

    @Query(value = "DELETE FROM money_transaction", nativeQuery = true)
    ResponseEntity<String> clearAll();


}
