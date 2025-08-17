package ranto.co.io.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ranto.co.io.model.Client;
import ranto.co.io.model.enums.TypeClient;

public interface ClientRepository extends JpaRepository<Client, Long> {
  List<Client> findByTypeClient(TypeClient typeClient);

  List<Client> findByNomContainingIgnoreCase(String nom);
}
