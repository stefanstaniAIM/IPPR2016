package at.fhjoanneum.ippr.processengine.akka.messages.process.timeout;

import org.apache.commons.lang3.StringUtils;

public class TimeoutScheduleStartMessage {

  private final Long userId;
  private final Long ssId;
  private String timeoutActorId;

  public TimeoutScheduleStartMessage(final Long userId, final Long ssId) {
    this.userId = userId;
    this.ssId = ssId;
  }

  public TimeoutScheduleStartMessage(final Long userId, final Long ssId,
      final String timeoutActorId) {
    this(userId, ssId);
    this.timeoutActorId = timeoutActorId;
  }

  public Long getUserId() {
    return userId;
  }

  public Long getSsId() {
    return ssId;
  }

  public String getTimeoutActorId() {
    return timeoutActorId == null ? StringUtils.EMPTY : timeoutActorId;
  }
}
