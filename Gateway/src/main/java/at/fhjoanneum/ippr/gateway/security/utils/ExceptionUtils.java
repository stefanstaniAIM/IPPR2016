package at.fhjoanneum.ippr.gateway.security.utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class ExceptionUtils {

  private ExceptionUtils() {}

  public static void createUnauthorizedException(final String msg, final ServletResponse res)
      throws IOException {
    ((HttpServletResponse) res).setStatus(401);
    ((HttpServletResponse) res).setHeader("ContentType", "plain/text");
    final PrintWriter writer = ((HttpServletResponse) res).getWriter();
    writer.write(msg);
    writer.close();
  }
}
