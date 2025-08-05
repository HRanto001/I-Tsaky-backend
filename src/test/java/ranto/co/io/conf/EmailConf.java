package ranto.co.io.conf;

import org.springframework.test.context.DynamicPropertyRegistry;
import ranto.co.io.PojaGenerated;

@PojaGenerated
public class EmailConf {

  void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("aws.ses.source", () -> "dummy-ses-source");
  }
}
