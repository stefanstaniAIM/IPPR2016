package at.fhjoanneum.ippr.eventlogger.services;

import javax.xml.transform.stream.StreamResult;
import java.util.Map;

public interface GenerateOWLService {

  StreamResult generateOWL(final String processModelName, final Map<String, String> petriNets) throws Exception;
}