package at.fhjoanneum.ippr.processengine.feign;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import at.fhjoanneum.ippr.persistence.entities.engine.state.SubjectStateImpl;
import at.fhjoanneum.ippr.persistence.objects.engine.subject.Subject;
import at.fhjoanneum.ippr.processengine.repositories.SubjectRepository;
import at.fhjoanneum.ippr.processengine.repositories.SubjectStateRepository;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class ProcessEngineFeignServiceImpl implements ProcessEngineFeignService {

  private final static Logger LOG = LoggerFactory.getLogger(ProcessEngineFeignServiceImpl.class);

  @Autowired
  private SubjectRepository subjectRepository;

  @Autowired
  private SubjectStateRepository subjectStateRepository;

  @Override
  public void markAsSent(final String transferId) {
    final String[] split = transferId.split("-");
    final Long piID = Long.valueOf(split[0]);
    final Long sId = Long.valueOf(split[1]);

    final Subject subject = subjectRepository.findOne(sId);
    subject.getSubjectState().setToSent();
    LOG.info("Marked as 'SENT' [{}]", subject.getSubjectState());

    subjectStateRepository.save((SubjectStateImpl) subject.getSubjectState());
  }

}
