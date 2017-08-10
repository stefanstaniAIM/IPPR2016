package at.fhjoanneum.ippr.eventlogger.services;

import javax.xml.transform.stream.StreamResult;

public interface ManipulatePNMLService {

  StreamResult manipulatePNML(final String pnmlContent, final String csvLog) throws Exception;

}