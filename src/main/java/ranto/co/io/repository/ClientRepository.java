package ranto.co.io.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ranto.co.io.model.Client;
import ranto.co.io.model.enums.TypeClient;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findByTypeClient(TypeClient typeClient);
    List<Client> findByNomContainingIgnoreCase(String nom);
}
