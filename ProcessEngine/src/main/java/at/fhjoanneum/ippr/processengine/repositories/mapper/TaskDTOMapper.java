package at.fhjoanneum.ippr.processengine.repositories.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.springframework.jdbc.core.RowMapper;

import at.fhjoanneum.ippr.commons.dto.processengine.TaskDTO;

public class TaskDTOMapper implements RowMapper<TaskDTO> {

  @Override
  public TaskDTO mapRow(final ResultSet rs, final int rowNum) throws SQLException {
    final Long piId = rs.getLong("pi_id");
    final String processName = rs.getString("process_name");
    final Long ssId = rs.getLong("ss_id");
    final String stateName = rs.getString("state_name");
    final String functionType = rs.getString("function_type");
    final LocalDateTime lastChanged = rs.getTimestamp("last_changed").toLocalDateTime();
    return new TaskDTO(piId, processName, ssId, stateName, functionType, lastChanged);
  }

}
