package at.fhjoanneum.ippr.processengine.repositories;

import java.util.List;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.SqlResultSetMapping;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import at.fhjoanneum.ippr.commons.dto.processengine.TaskDTO;
import at.fhjoanneum.ippr.persistence.entities.engine.state.SubjectStateImpl;

@SqlResultSetMapping(name = "taskDTOMapping",
    classes = {@ConstructorResult(targetClass = TaskDTO.class,
        columns = {@ColumnResult(name = "GROUP_ID"), @ColumnResult(name = "USER_ID")})})
public interface SubjectStateRepository extends CrudRepository<SubjectStateImpl, Long> {

  @Query(value = "SELECT * FROM subject_state WHERE pi_id = :piId", nativeQuery = true)
  List<SubjectStateImpl> getSubjectStatesOfProcessInstance(@Param("piId") Long piId);
}
