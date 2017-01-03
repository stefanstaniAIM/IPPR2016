package at.fhjoanneum.ippr.processengine.repositories;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Maps;

import at.fhjoanneum.ippr.commons.dto.processengine.TaskDTO;
import at.fhjoanneum.ippr.processengine.repositories.mapper.TaskDTOMapper;

@Repository
public class CustomTypesQueriesRepositoryImpl implements CustomTypesQueriesRepository {

  private final static Logger LOG = LoggerFactory.getLogger(CustomTypesQueriesRepositoryImpl.class);

  private static class Queries {
    public static String TASK_QUERY =
        " SELECT pi.pi_id, pm.name process_name, ss.ss_id, state.name state_name, state.function_type, ss.last_changed       "
            + "   FROM SUBJECT_STATE ss                                                                                      "
            + "   JOIN STATE state ON state.s_id = ss.current_state                                                          "
            + "   JOIN PROCESS_SUBJECT_INSTANCE_MAP psim ON psim.s_id = ss.s_id                                              "
            + "   JOIN PROCESS_INSTANCE pi ON pi.pi_id = psim.pi_id                                                          "
            + "   JOIN SUBJECT s ON s.s_id = ss.s_id                                                                         "
            + "   JOIN PROCESS_MODEL pm ON pm.pm_id = pi.pm_id                                                               "
            + "   WHERE s.user_id = :userId                                                                                  "
            + "   AND pi.state = 'ACTIVE'                                                                                    "
            + "   AND state.function_type IN ('FUNCTION', 'SEND')                                                            "
            + "   OR (state.function_type = 'RECEIVE' AND ss.sub_state = 'RECEIVED')                                         "
            + "   ORDER BY ss.last_changed ASC                                                                               ";
  }


  @Autowired
  private NamedParameterJdbcTemplate entityManager;

  @Override
  public List<TaskDTO> getTasksOfUser(final Long userId) {
    final Map<String, Object> params = Maps.newHashMap();
    params.put("userId", userId);

    final List<TaskDTO> tasks =
        entityManager.query(Queries.TASK_QUERY, params, new TaskDTOMapper());
    return tasks;
  }
}
