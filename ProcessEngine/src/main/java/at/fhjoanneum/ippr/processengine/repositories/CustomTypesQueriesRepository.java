package at.fhjoanneum.ippr.processengine.repositories;

import java.util.List;

import at.fhjoanneum.ippr.commons.dto.processengine.TaskDTO;

public interface CustomTypesQueriesRepository {

  List<TaskDTO> getTasksOfUser(Long userId);
}
