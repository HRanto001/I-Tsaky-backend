// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.mockito.Mockito.when;
//
// import java.time.LocalDate;
// import java.time.LocalDateTime;
// import java.util.List;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.Mockito;
// import ranto.co.io.endpoint.controller.StatsController;
// import ranto.co.io.model.Commande;
// import ranto.co.io.model.CommandeDetail;
// import ranto.co.io.model.Depense;
// import ranto.co.io.repository.CommandeRepository;
// import ranto.co.io.repository.DepenseRepository;
//
// class StatsControllerTest {
//
//  private DepenseRepository depenseRepository;
//  private CommandeRepository commandeRepository;
//  private StatsController statsController;
//
//  @BeforeEach
//  void setUp() {
//    depenseRepository = Mockito.mock(DepenseRepository.class);
//    commandeRepository = Mockito.mock(CommandeRepository.class);
//    statsController = new StatsController(depenseRepository, commandeRepository);
//  }
//
//  @Test
//  void testDepensesParJour() {
//    Depense d1 = new Depense();
//    d1.setMontant(5000.0);
//    d1.setDateDepense(LocalDate.of(2025, 8, 19));
//
//    Depense d2 = new Depense();
//    d2.setMontant(2000.0);
//    d2.setDateDepense(LocalDate.of(2025, 8, 19));
//
//    when(depenseRepository.findAll()).thenReturn(List.of(d1, d2));
//
//    var result = statsController.getDepensesParJour();
//    assertEquals(7000.0, result.get(LocalDate.of(2025, 8, 19)));
//  }
//
//  @Test
//  void testRevenus() {
//    Commande c1 = new Commande();
//    c1.setDateCommande(LocalDateTime.of(2025, 8, 19, 10, 0));
//
//    CommandeDetail detail1 = new CommandeDetail();
//    detail1.setPrixTotal(12000.0);
//
//    CommandeDetail detail2 = new CommandeDetail();
//    detail2.setPrixTotal(8000.0);
//
//    c1.setDetails(List.of(detail1, detail2));
//
//    when(commandeRepository.findAll()).thenReturn(List.of(c1));
//
//    double revenus =
//        statsController.getRevenus(LocalDate.of(2025, 8, 1), LocalDate.of(2025, 8, 31));
//
//    assertEquals(20000.0, revenus);
//  }
//
//  @Test
//  void testBenefice() {
//    // Dépenses
//    Depense d1 = new Depense();
//    d1.setMontant(5000.0);
//    d1.setDateDepense(LocalDate.of(2025, 8, 19));
//
//    when(depenseRepository.findAll()).thenReturn(List.of(d1));
//
//    // Revenus
//    Commande c1 = new Commande();
//    c1.setDateCommande(LocalDateTime.of(2025, 8, 19, 10, 0));
//
//    CommandeDetail detail1 = new CommandeDetail();
//    detail1.setPrixTotal(15000.0);
//
//    c1.setDetails(List.of(detail1));
//
//    when(commandeRepository.findAll()).thenReturn(List.of(c1));
//
//    double benefice =
//        statsController.getBenefice(LocalDate.of(2025, 8, 1), LocalDate.of(2025, 8, 31));
//
//    // 15000 - 5000 = 10000
//    assertEquals(10000.0, benefice);
//  }
// }
