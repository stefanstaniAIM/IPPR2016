package at.fhjoanneum.ippr.processengine.akka.messages.process.check;

public class ProcessCheckResponseMessage {

  private final boolean isCorrect;

  public ProcessCheckResponseMessage(final boolean isCorrect) {
    this.isCorrect = isCorrect;
  }

  public boolean isCorrect() {
    return isCorrect;
  }
}
