package at.fhjoanneum.ippr.processengine.feign;

public interface ProcessEngineFeignService {

  void markAsSent(String transferId);
}
