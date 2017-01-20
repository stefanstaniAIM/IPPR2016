package at.fhjoanneum.ippr.pmstorage.examples;

import java.io.File;
import java.io.FileReader;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;



@Component
@Transactional
public class VacationRequestFromOWL extends AbstractExample {

  private static final Logger LOG = LoggerFactory.getLogger(VacationRequestFromOWL.class);

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  protected EntityManager getEntityManager() {
    return entityManager;
  }

  @Override
  protected void createData() {
	  String filename = "univ.owl";
	  try {
	  	File file = new File(filename);
	  	FileReader reader = new FileReader(file);
	  	OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
	  	
	  	model.read(reader,null);
	  	model.write(System.out,"RDF/XML-ABBREV");
	  } catch (Exception e) {
	  	e.printStackTrace();
	  }
   
  }

  @Override
  protected String getName() {
    return "Vacation Request From OWL";
  }
}
