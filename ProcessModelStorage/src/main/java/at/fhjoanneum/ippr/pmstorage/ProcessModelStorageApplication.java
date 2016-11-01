package at.fhjoanneum.ippr.pmstorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("at.fhjoanneum.ippr.persistence.entities")
public class ProcessModelStorageApplication {

	public static void main(final String[] args) {
		SpringApplication.run(ProcessModelStorageApplication.class, args);
	}
}
