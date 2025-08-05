package ranto.co.io.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import ranto.co.io.entity.Help;

@Repository
public class HelpRepository {

  private final List<Help> helpList = new ArrayList<>();
  private long nextId = 1;

  // Simuler un ID si besoin (à gérer dans Help si tu ajoutes un champ id)
  public Help save(Help help) {
    helpList.add(help);
    return help;
  }

  public List<Help> findAll() {
    return new ArrayList<>(helpList);
  }

  public Optional<Help> findByIndex(int index) {
    if (index >= 0 && index < helpList.size()) {
      return Optional.of(helpList.get(index));
    }
    return Optional.empty();
  }

  public boolean delete(Help help) {
    return helpList.remove(help);
  }

  public void clear() {
    helpList.clear();
  }
}
