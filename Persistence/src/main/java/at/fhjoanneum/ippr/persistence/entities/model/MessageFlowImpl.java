package at.fhjoanneum.ippr.persistence.entities.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.base.Objects;

import at.fhjoanneum.ippr.persistence.objects.model.MessageFlow;
import at.fhjoanneum.ippr.persistence.objects.model.SubjectModel;

@Entity(name = "MESSAGE_FLOW")
public class MessageFlowImpl implements MessageFlow, Serializable {

	private static final long serialVersionUID = 8061014521953539089L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long mfId;

	@ManyToOne
	@JoinColumn(name = "sender")
	private SubjectModelImpl sender;

	@ManyToOne
	@JoinColumn(name = "receiver")
	private SubjectModelImpl receiver;

	@Override
	public Long getMfId() {
		return mfId;
	}

	@Override
	public SubjectModel getSender() {
		return sender;
	}

	@Override
	public SubjectModel getReceiver() {
		return receiver;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (!MessageFlow.class.isAssignableFrom(obj.getClass())) {
			return false;
		}
		final MessageFlow other = (MessageFlow) obj;
		if ((this.mfId == null) ? (other.getMfId() != null) : !this.mfId.equals(other.getMfId())) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(mfId);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("mfId", mfId).toString();
	}
}
