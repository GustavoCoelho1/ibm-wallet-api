package br.com.ibmwallet.services;

import br.com.ibmwallet.dtos.ClientDTO;
import br.com.ibmwallet.dtos.LoginRequestDTO;
import br.com.ibmwallet.entities.Client;
import br.com.ibmwallet.repositories.ClientRepository;
import com.google.gson.Gson;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;


    public List<ClientDTO> getAll() {
        List<Client> categories = clientRepository.findAll();
        List<ClientDTO> response = new ArrayList<>();

        for (Client client:categories) {
            response.add(new ClientDTO(client));
        }

        return response;
    }

    private String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    public ResponseEntity<String> save(ClientDTO data) {
        if (data.name() == null || data.email() == null || data.password() == null) {
            return new ResponseEntity<>("Todos os dados são necessários para cadastrar o registro!", HttpStatus.BAD_REQUEST);
        }

        if(clientRepository.findByEmail(data.email()) == null) {
            Client client = new Client(data);

            String encryptedPassword = encryptPassword(client.getPassword());
            client.setPassword(encryptedPassword);

            Client newClient = clientRepository.save(client);

            if (newClient.getId() != null) {
                return new ResponseEntity<>("Registro cadastrado com sucesso!", HttpStatus.OK);
            }
            return new ResponseEntity<>("Houve um erro ao cadastrar o registro!", HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>("Registro já cadastrado!", HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<String> update(Long id, ClientDTO data) {
        Optional<Client> client = clientRepository.findById(id);

        if (client.isPresent()) {
            Client newClient = new Client(data);
            newClient.setId(client.get().getId());

            if (data.name() != null) { newClient.setName(data.name()); }
            if (data.email() != null) { newClient.setEmail(data.email()); }
            if (data.password() != null) {
                String encryptedPassword = encryptPassword(data.password());
                newClient.setPassword(encryptedPassword);
            }

            Client updatedClient = clientRepository.save(newClient);

            if (updatedClient.getId() != null) {
                return new ResponseEntity<>("Registro atualizado com sucesso!", HttpStatus.OK);
            }
            return new ResponseEntity<>("Houve um erro ao atualizar o registro!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
    }

    public ResponseEntity<String> delete(Long id) {
        Optional<Client> client = clientRepository.findById(id);

        if (client.isPresent()) {
            clientRepository.delete(client.get());

            if (clientRepository.findById(id).isEmpty()) {
                return new ResponseEntity<>("Registro apagado com sucesso!", HttpStatus.OK);
            }
            return new ResponseEntity<>("Houve um erro ao apagar o registro!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("ID inexistente!", HttpStatus.BAD_REQUEST);
    }

    boolean validatePassword(String rawPassword, String hashPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, hashPassword);
    }

    public ResponseEntity<String> login(LoginRequestDTO data) {
        Client client = clientRepository.findByEmail(data.email());

        if (client == null) {
            return new ResponseEntity<>("E-mail e/ou senha inválidos!", HttpStatus.BAD_REQUEST);
        } else if (!validatePassword(data.password(), client.getPassword())) {
            return new ResponseEntity<>("E-mail e/ou senha inválidos!", HttpStatus.BAD_REQUEST);
        } else {
            Date now = new Date();
            Date expirationDate = new Date(now.getTime() + 3600000); // 1 hora

            try {
                String response = Jwts.builder()
                        .setSubject(client.getName())
                        .claim("email", client.getEmail())
                        .claim("client_id", client.getId())
                        .setIssuedAt(now)
                        .setExpiration(expirationDate)
                        .signWith(SignatureAlgorithm.HS256 , jwtSecret)
                        .compact();

                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (Exception e) {
                System.out.print(e);
                return new ResponseEntity<>("Houve um problema ao realizar o login, tente novamente mais tarde", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
}
