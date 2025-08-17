package ranto.co.io.service;

import org.springframework.stereotype.Service;
import ranto.co.io.model.Client;
import ranto.co.io.model.enums.TypeClient;
import ranto.co.io.repository.ClientRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public Optional<Client> findById(Long id) {
        return clientRepository.findById(id);
    }

    public List<Client> findByTypeClient(TypeClient typeClient) {
        return clientRepository.findByTypeClient(typeClient);
    }

    public List<Client> findByNomContaining(String nom) {
        return clientRepository.findByNomContainingIgnoreCase(nom);
    }

    public Client save(Client client) {
        return clientRepository.save(client);
    }

    public void delete(Long id) {
        clientRepository.deleteById(id);
    }
}
