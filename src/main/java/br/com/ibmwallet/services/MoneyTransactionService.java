package br.com.ibmwallet.services;

import br.com.ibmwallet.dtos.ClientDTO;
import br.com.ibmwallet.dtos.MoneyTransactionDTO;
import br.com.ibmwallet.dtos.MoneyTransactionFormattedDTO;
import br.com.ibmwallet.entities.Category;
import br.com.ibmwallet.entities.Client;
import br.com.ibmwallet.entities.MoneyTransaction;
import br.com.ibmwallet.entities.Recipient;
import br.com.ibmwallet.repositories.CategoryRepository;
import br.com.ibmwallet.repositories.ClientRepository;
import br.com.ibmwallet.repositories.MoneyTransactionRepository;
import br.com.ibmwallet.repositories.RecipientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MoneyTransactionService {
    @Autowired
    private MoneyTransactionRepository moneyTransactionRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private RecipientRepository recipientRepository;
    @Autowired
    private ClientRepository clientRepository;


    public List<MoneyTransactionDTO> getAll() {
        List<MoneyTransaction> moneyTransactions = moneyTransactionRepository.findAll();
        List<MoneyTransactionDTO> response = new ArrayList<>();

        for (MoneyTransaction moneyTransaction:moneyTransactions) {
            response.add(new MoneyTransactionDTO(moneyTransaction));
        }

        return response;
    }

    public List<MoneyTransactionDTO> getAllByUserId(Long id) {
        List<MoneyTransaction> moneyTransactions = moneyTransactionRepository.findAllByClientId(id);
        List<MoneyTransactionDTO> response = new ArrayList<>();

        for (MoneyTransaction moneyTransaction:moneyTransactions) {
            response.add(new MoneyTransactionDTO(moneyTransaction));
        }

        return response;
    }

    public ResponseEntity<String> save(MoneyTransactionDTO data) {
        if (data.value() == null || data.category_id() == null || data.recipient_id() == null || data.client_id() == null) {
            return new ResponseEntity<>("Todos os dados são necessários para cadastrar o registro!", HttpStatus.BAD_REQUEST);
        }

        Optional<Client> client = clientRepository.findById(data.client_id());
        Optional<Category> category = categoryRepository.findById(data.category_id());
        Optional<Recipient> recipient = recipientRepository.findById(data.recipient_id());

        if (client.isEmpty() || category.isEmpty() || recipient.isEmpty()) {
            return new ResponseEntity<>("Um ou mais dados são inválidos para o cadastro do registro!", HttpStatus.BAD_REQUEST);
        }

        MoneyTransaction moneyTransaction = new MoneyTransaction(data);
        moneyTransaction.setClient(client.get());
        moneyTransaction.setCategory(category.get());
        moneyTransaction.setRecipient(recipient.get());

        MoneyTransaction newMoneyTransaction = moneyTransactionRepository.save(moneyTransaction);

        if (newMoneyTransaction.getId() != null) {
            return new ResponseEntity<>("Registro cadastrado com sucesso!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Houve um erro ao cadastrar o registro!", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> update(Long id, MoneyTransactionDTO data) {
        Optional<MoneyTransaction> moneyTransaction = moneyTransactionRepository.findById(id);

        if (moneyTransaction.isPresent()) {
            MoneyTransaction newMoneyTransaction = new MoneyTransaction(data);
            //Setando valores anteriores para transação
            newMoneyTransaction.setId(moneyTransaction.get().getId());
            newMoneyTransaction.setCategory(moneyTransaction.get().getCategory());
            newMoneyTransaction.setRecipient(moneyTransaction.get().getRecipient());
            newMoneyTransaction.setClient(moneyTransaction.get().getClient());

            if (data.value() != null) { newMoneyTransaction.setValue(data.value()); }
            if (data.category_id() != null) {
                Optional<Category> newCategory = categoryRepository.findById(data.category_id());

                if (newCategory.isPresent()) {
                    newMoneyTransaction.setCategory(newCategory.get());
                }
            }
            if (data.recipient_id() != null) {
                Optional<Recipient> newRecipient = recipientRepository.findById(data.recipient_id());

                if (newRecipient.isPresent()) {
                    newMoneyTransaction.setRecipient(newRecipient.get());
                }
            }

            //Validações de segurança
            if (data.created_at() != null) {
                return new ResponseEntity<>("Não é possível alterar a data da transação!", HttpStatus.BAD_REQUEST);
            }

            if (data.client_id() != null) {
                return new ResponseEntity<>("Não é possível alterar o emissor da transação!", HttpStatus.BAD_REQUEST);
            }

            MoneyTransaction updatedMoneyTransaction = moneyTransactionRepository.save(newMoneyTransaction);

            if (updatedMoneyTransaction.getId() != null) {
                return new ResponseEntity<>("Registro atualizado com sucesso!", HttpStatus.OK);
            }
            return new ResponseEntity<>("Houve um erro ao atualizar o registro!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
    }

    public ResponseEntity<String> delete(Long id) {
        Optional<MoneyTransaction> moneyTransaction = moneyTransactionRepository.findById(id);

        if (moneyTransaction.isPresent()) {
            moneyTransactionRepository.delete(moneyTransaction.get());

            if (moneyTransactionRepository.findById(id).isEmpty()){
                return new ResponseEntity<>("Registro apagado com sucesso!", HttpStatus.OK);
            }
            return new ResponseEntity<>("Houve um erro ao apagar o registro!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("ID inexistente!", HttpStatus.BAD_REQUEST);
    }
}
