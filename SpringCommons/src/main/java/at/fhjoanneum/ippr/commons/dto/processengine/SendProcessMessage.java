package at.fhjoanneum.ippr.commons.dto.processengine;

public class SendProcessMessage {

  public static class Request {
    private final Long piID;
    private final Long subjectPartnerId;
    private final Long userId;
    private final Long mfId;
    private final Long boId;

    public Request(final Long piID, final Long subjectPartnerId, final Long userId, final Long mfId,
        final Long boId) {
      this.piID = piID;
      this.subjectPartnerId = subjectPartnerId;
      this.userId = userId;
      this.mfId = mfId;
      this.boId = boId;
    }

    public Long getPiID() {
      return piID;
    }

    public Long getSubjectPartnerId() {
      return subjectPartnerId;
    }

    public Long getUserId() {
      return userId;
    }

    public Long getMfId() {
      return mfId;
    }

    public Long getBoId() {
      return boId;
    }
  }

  public static class Response {
  }
}
