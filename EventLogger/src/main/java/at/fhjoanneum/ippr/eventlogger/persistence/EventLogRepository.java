package at.fhjoanneum.ippr.eventlogger.persistence;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventLogRepository extends CrudRepository<EventLogEntry, Long> {
    @Query(value = "SELECT * FROM event_log AS log WHERE log.process_model_id = :processModelId AND log.resource = :subject AND EXISTS((SELECT 1 from event_log AS x " +
            "WHERE x.case_id = log.case_id AND x.activity = 'Process Start' AND x.message_type = '' AND x.resource = '' AND x.state = '')) " +
            "AND EXISTS((SELECT 1 from event_log AS y WHERE y.case_id = log.case_id AND y.activity = 'Process End' AND y.message_type = '' AND y.resource = '' AND y.state = ''))" +
            "ORDER BY log.case_id, log.event_id", nativeQuery = true)
    public List<EventLogEntry> getEventLogForProcessModelAndSubject(@Param("processModelId") int processModelId, @Param("subject") String subject);
}
