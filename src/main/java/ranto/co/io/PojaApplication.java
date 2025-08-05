package ranto.co.io;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@PojaGenerated
@EnableScheduling
@SpringBootApplication(scanBasePackages = "ranto.co.io")
@EntityScan(basePackages = "ranto.co.io.entity")
public class PojaApplication {

  public static void main(String[] args) {
    SpringApplication.run(PojaApplication.class, args);
  }
}
