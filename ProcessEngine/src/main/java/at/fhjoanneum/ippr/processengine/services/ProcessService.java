package at.fhjoanneum.ippr.processengine.services;

import java.util.concurrent.Future;

public interface ProcessService {

  Future<Object> startProcess(Long processId);
}
