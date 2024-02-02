package br.com.ibmwallet.services;

import br.com.ibmwallet.dtos.LargeScaleSaveRequestDTO;
import br.com.ibmwallet.dtos.MoneyTransactionDTO;
import br.com.ibmwallet.entities.Category;
import br.com.ibmwallet.entities.Client;
import br.com.ibmwallet.entities.MoneyTransaction;
import br.com.ibmwallet.entities.Recipient;
import br.com.ibmwallet.repositories.CategoryRepository;
import br.com.ibmwallet.repositories.ClientRepository;
import br.com.ibmwallet.repositories.MoneyTransactionRepository;
import br.com.ibmwallet.repositories.RecipientRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    @PersistenceContext
    private EntityManager entityManager;


    public List<MoneyTransactionDTO> getAll() {
        List<MoneyTransaction> moneyTransactions = moneyTransactionRepository.findAll();
        List<MoneyTransactionDTO> response = new ArrayList<>();

        for (MoneyTransaction moneyTransaction:moneyTransactions) {
            response.add(new MoneyTransactionDTO(moneyTransaction));
        }

        return response;
    }

    public List<MoneyTransactionDTO> getAllByClientId(Long id) {
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

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Erro aqui");
            return false;
        }
    }

    private boolean isValidDate(String date) {
        String[] dateArr = date.split("-");

        if (dateArr.length != 3) {
            return false;
        }

        boolean yearNaN = !isNumeric(dateArr[0]);
        boolean monthNaN = !isNumeric(dateArr[1]);
        boolean dayNaN = !isNumeric(dateArr[2]);

        if (dateArr[0].length() != 4 || yearNaN) {
            // Campo de ano, ano deve ter 4 dígitos e deve ser um número
            return false;
        }

        if (dateArr[1].length() != 2 || monthNaN || Integer.parseInt(dateArr[1]) < 1 || Integer.parseInt(dateArr[1]) > 12) {
            // Campo de mês, mês deve ter 2 dígitos, deve ser um número e deve estar entre o período de 1 e 12
            return false;
        }

        if (dateArr[2].length() != 2 || dayNaN || Integer.parseInt(dateArr[2]) < 1 || Integer.parseInt(dateArr[2]) > 31) {
            // Campo de dia, dia deve ter 2 dígitos, deve ser um número e deve estar entre o período de 1 e 31
            return false;
        }

        return true;
    }

    private boolean isValidValue(String value) {
        return isNumeric(value);
    }

    private Long getCategoryId(List<Category> categoryList, String categoryName) {
        Optional<Category> categoryInList = categoryList.stream()
                .filter(cat -> cat.getName().equals(categoryName)).findFirst();

        if (categoryInList.isPresent()) {
            return categoryInList.get().getId();
        }

        return 0L; //Retorna zero se não encontrar
    }

    private Long getRecipientId(List<Recipient> recipientList, String recipientName) {
        Optional<Recipient> recipientInList = recipientList.stream()
                .filter(rec -> rec.getName().equals(recipientName)).findFirst();

        if (recipientInList.isPresent()) {
            return recipientInList.get().getId();
        }

        return 0L; //Retorna zero se não encontrar
    }

    @Transactional
    public ResponseEntity<String> largeScaleSave(LargeScaleSaveRequestDTO data) { //Informação virá no formato: [["<data>", "<valor>", "<categoria>", "<destinatário/remetente>"], ["<data>", "<valor>", "<categoria>", "<destinatário/remetente>"]]
        if (data.client_id() == null || data.dataList().isEmpty()) {
            return new ResponseEntity<>("Todos os dados são necessários para cadastrar os registros!", HttpStatus.BAD_REQUEST);
        }

        Optional<Client> client = clientRepository.findById(data.client_id());
        if (client.isEmpty()) {
            return new ResponseEntity<>("ID de cliente inválido para o cadastro do registro!", HttpStatus.BAD_REQUEST);
        }

        Long clientId = client.get().getId();

        List<List<String>> dataList = data.dataList();

        List<Category> categoryList = categoryRepository.findAll();
        List<Recipient> recipientList = recipientRepository.findAll();

        int list_register = 1;

        ResponseEntity<String> errorMsg = new ResponseEntity<>("Dados inválidos no registro núm. " + list_register + "!", HttpStatus.BAD_REQUEST);

        for (List<String> transactionData:dataList) { //Valida os dados de cada uma dos registros, e retorna o número (index) da linha incorreta
            if (transactionData.size() != 4) {
                return errorMsg;
            }

            String dateArr = transactionData.get(0);
            String valueStr = transactionData.get(1);
            String categoryName = transactionData.get(2);
            String recipientName = transactionData.get(3);

            Long categoryId = getCategoryId(categoryList, categoryName);
            Long recipientId = getRecipientId(recipientList, recipientName);

            if (!isValidDate(dateArr) || !isValidValue(valueStr) || categoryId == 0 || recipientId == 0) {
                return errorMsg;
            }

            Double value = Double.parseDouble(valueStr);
            LocalDate date = LocalDate.parse(dateArr);

            //Rodando query nativamente para melhorar a performace da operação
            entityManager.createNativeQuery("INSERT INTO money_transaction (client_id, date, value, category_id, recipient_id) VALUES (?, ?, ?, ?, ?)")
                    .setParameter(1, clientId)
                    .setParameter(2, date)
                    .setParameter(3, value)
                    .setParameter(4, categoryId)
                    .setParameter(5, recipientId)
                    .executeUpdate();
        }

        return new ResponseEntity<>("Registros inseridos com sucesso!", HttpStatus.OK);
    }

    public ResponseEntity<String> update(Long id, MoneyTransactionDTO data) {
        //Validações de segurança
        if (data.client_id() != null) {
            return new ResponseEntity<>("Não é possível alterar o emissor do registro!", HttpStatus.BAD_REQUEST);
        }

        Optional<MoneyTransaction> moneyTransaction = moneyTransactionRepository.findById(id);

        if (moneyTransaction.isPresent()) {
            MoneyTransaction newMoneyTransaction = new MoneyTransaction(data);
            //Setando valores anteriores para transação
            newMoneyTransaction.setId(moneyTransaction.get().getId());
            newMoneyTransaction.setCategory(moneyTransaction.get().getCategory());
            newMoneyTransaction.setRecipient(moneyTransaction.get().getRecipient());
            newMoneyTransaction.setClient(moneyTransaction.get().getClient());

            if (data.value() != null) { newMoneyTransaction.setValue(data.value()); }
            if (data.date() != null) {
                newMoneyTransaction.setDate(data.date());
            }
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

    public ResponseEntity<String> clearAll() {
        try {
            moneyTransactionRepository.clearAll();

            if (moneyTransactionRepository.findAll().isEmpty()) {
                return new ResponseEntity<>("Base de dados limpa com sucesso!", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Houve um erro ao limpar a base de dados!", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Houve um erro ao limpar a base de dados!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
