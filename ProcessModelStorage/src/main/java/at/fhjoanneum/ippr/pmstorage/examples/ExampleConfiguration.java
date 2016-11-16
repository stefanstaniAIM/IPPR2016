package at.fhjoanneum.ippr.pmstorage.examples;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExampleConfiguration {

  @Value("${ippr.insert-examples.enabled}")
  private boolean insertExamplesEnabled;

  public boolean isInsertExamplesEnabled() {
    return insertExamplesEnabled;
  }
}
