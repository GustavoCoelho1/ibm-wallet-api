package br.com.ibmwallet.services;

import br.com.ibmwallet.dtos.RecipientDTO;
import br.com.ibmwallet.entities.Client;
import br.com.ibmwallet.entities.Recipient;
import br.com.ibmwallet.repositories.ClientRepository;
import br.com.ibmwallet.repositories.RecipientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecipientService {
    @Autowired
    private RecipientRepository recipientRepository;
    @Autowired
    private ClientRepository clientRepository;

    public List<RecipientDTO> getAll() {
        List<Recipient> categories = recipientRepository.findAll();
        List<RecipientDTO> response = new ArrayList<>();

        for (Recipient recipient:categories) {
            response.add(new RecipientDTO(recipient));
        }

        return response;
    }

    public List<RecipientDTO> getAllByClientId(Long id) {
        List<Recipient> recipients = recipientRepository.findAllByClientId(id);
        List<RecipientDTO> response = new ArrayList<>();

        for (Recipient recipient:recipients) {
            response.add(new RecipientDTO(recipient));
        }

        return response;
    }

    public ResponseEntity<String> save(RecipientDTO data) {
        if(recipientRepository.findByName(data.name()).isEmpty()) {
            if (data.name() == null || data.client_id() == null) {
                return new ResponseEntity<>("Todos os dados são necessários para cadastrar o registro!", HttpStatus.BAD_REQUEST);
            }

            Optional<Client> client = clientRepository.findById(data.client_id());

            if (client.isEmpty()) {
                return new ResponseEntity<>("Um ou mais dados são inválidos para o cadastro do registro!", HttpStatus.BAD_REQUEST);
            }

            Recipient recipient = new Recipient(data);
            recipient.setClient(client.get());

            Recipient newRecipient = recipientRepository.save(recipient);

            if (newRecipient.getId() != null) {
                return new ResponseEntity<>("Registro cadastrado com sucesso!", HttpStatus.OK);
            }
            return new ResponseEntity<>("Houve um erro ao cadastrar o registro!", HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>("Registro já cadastrado!", HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<String> update(Long id, RecipientDTO data) {
        //Validações de segurança
        if (data.client_id() != null) {
            return new ResponseEntity<>("Não é possível alterar o emissor do registro!", HttpStatus.BAD_REQUEST);
        }

        Optional<Recipient> recipient = recipientRepository.findById(id);

        if (recipient.isPresent() && data.name() != null) {
            Recipient newRecipient = new Recipient(data);
            newRecipient.setId(recipient.get().getId());
            newRecipient.setClient(recipient.get().getClient());
            newRecipient.setName(data.name());

            Recipient updatedRecipient = recipientRepository.save(newRecipient);

            if (updatedRecipient.getId() != null) {
                return new ResponseEntity<>("Registro atualizado com sucesso!", HttpStatus.OK);
            }
            return new ResponseEntity<>("Houve um erro ao atualizar o registro!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
    }

    public ResponseEntity<String> delete(Long id) {
        Optional<Recipient> recipient = recipientRepository.findById(id);

        if (recipient.isPresent()) {
            recipientRepository.delete(recipient.get());

           if (recipientRepository.findById(id).isEmpty()) {
               return new ResponseEntity<>("Registro apagado com sucesso!", HttpStatus.OK);
           }
            return new ResponseEntity<>("Houve um erro ao apagar o registro!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("ID inexistente!", HttpStatus.BAD_REQUEST);
    }
}
