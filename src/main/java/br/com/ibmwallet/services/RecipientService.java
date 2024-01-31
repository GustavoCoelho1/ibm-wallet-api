package br.com.ibmwallet.services;

import br.com.ibmwallet.dtos.RecipientDTO;
import br.com.ibmwallet.entities.Recipient;
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

    public List<RecipientDTO> getAll() {
        List<Recipient> categories = recipientRepository.findAll();
        List<RecipientDTO> response = new ArrayList<>();

        for (Recipient recipient:categories) {
            response.add(new RecipientDTO(recipient));
        }

        return response;
    }

    public ResponseEntity<String> save(RecipientDTO data) {
        if (data.name() == null) {
            return new ResponseEntity<>("Todos os dados são necessários para cadastrar o registro!", HttpStatus.BAD_REQUEST);
        }

        if(recipientRepository.findByName(data.name()).isEmpty()) {
            Recipient recipient = new Recipient(data);
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
        Optional<Recipient> recipient = recipientRepository.findById(id);

        if (recipient.isPresent() && data.name() != null) {
            Recipient newRecipient = new Recipient(data);
            newRecipient.setId(recipient.get().getId());
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
