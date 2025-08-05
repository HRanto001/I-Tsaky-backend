package ranto.co.io.conf;

import org.springframework.test.context.DynamicPropertyRegistry;
import ranto.co.io.PojaGenerated;

@PojaGenerated
public class BucketConf {

  void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("aws.s3.bucket", () -> "dummy-bucket");
  }
}
