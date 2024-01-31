package br.com.ibmwallet.repositories;

import br.com.ibmwallet.dtos.MoneyTransactionFormattedDTO;
import br.com.ibmwallet.entities.Client;
import br.com.ibmwallet.entities.MoneyTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MoneyTransactionRepository extends JpaRepository<MoneyTransaction, Long> {
    @Query(value = "SELECT * FROM money_transaction WHERE client_id = :id", nativeQuery = true)
    List<MoneyTransaction> findAllByClientId(@Param("id") Long id);
}
