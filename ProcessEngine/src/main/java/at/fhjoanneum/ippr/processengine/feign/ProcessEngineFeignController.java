package at.fhjoanneum.ippr.processengine.feign;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import at.fhjoanneum.ippr.commons.dto.communicator.ExternalOutputMessage;
import at.fhjoanneum.ippr.persistence.entities.engine.state.SubjectStateImpl;
import at.fhjoanneum.ippr.persistence.objects.engine.subject.Subject;
import at.fhjoanneum.ippr.processengine.repositories.SubjectRepository;
import at.fhjoanneum.ippr.processengine.repositories.SubjectStateRepository;

@RestController
@Transactional
public class ProcessEngineFeignController {

  private final static Logger LOG = LoggerFactory.getLogger(ProcessEngineFeignController.class);

  @Autowired
  private SubjectRepository subjectRepository;

  @Autowired
  private SubjectStateRepository subjectStateRepository;

  @RequestMapping(value = "pe/markAsSent/{transferId}")
  public void markAsSent(@PathVariable("transferId") final String transferId) {
    final String[] split = transferId.split("-");
    final Long piID = Long.valueOf(split[0]);
    final Long sId = Long.valueOf(split[1]);

    final Subject subject = subjectRepository.findOne(sId);
    subject.getSubjectState().setToSent();
    LOG.info("Marked as 'SENT' [{}]", subject.getSubjectState());

    subjectStateRepository.save((SubjectStateImpl) subject.getSubjectState());
  }

  @RequestMapping(value = "pe/receive", method = RequestMethod.POST)
  public void receive(@RequestBody final ExternalOutputMessage msg) {
    LOG.debug("Received [{}]", msg);
  }
}
