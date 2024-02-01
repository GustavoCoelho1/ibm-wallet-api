package br.com.ibmwallet.repositories;

import br.com.ibmwallet.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByName(String name);

    @Query(value = "SELECT * FROM category WHERE client_id = :id", nativeQuery = true)
    List<Category> findAllByClientId(@Param("id") Long id);
}
