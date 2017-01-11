package at.fhjoanneum.ippr.processengine.config;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

public class FormatConfig {
  public final static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  public final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.00");
  public final static DateTimeFormatter TIMESTAMP_FORMAT =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
}
