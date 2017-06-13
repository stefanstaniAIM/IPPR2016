package at.fhjoanneum.ippr.persistence.entities.model.messageflow;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import at.fhjoanneum.ippr.persistence.entities.model.businessobject.BusinessObjectModelImpl;
import at.fhjoanneum.ippr.persistence.entities.model.process.ProcessModelImpl;
import at.fhjoanneum.ippr.persistence.entities.model.state.StateImpl;
import at.fhjoanneum.ippr.persistence.entities.model.subject.SubjectModelImpl;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.BusinessObjectModel;
import at.fhjoanneum.ippr.persistence.objects.model.messageflow.MessageFlow;
import at.fhjoanneum.ippr.persistence.objects.model.process.ProcessModel;
import at.fhjoanneum.ippr.persistence.objects.model.subject.SubjectModel;

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

  @ManyToOne
  @JoinColumn(name = "s_id")
  private StateImpl state;

  @ManyToMany
  @JoinTable(name = "business_object_model_message_flow_map",
      joinColumns = {@JoinColumn(name = "mf_id")},
      inverseJoinColumns = {@JoinColumn(name = "bom_id")})
  private List<BusinessObjectModelImpl> businessObjectModels = Lists.newArrayList();

  @ManyToOne
  @JoinColumn(name = "pm_id")
  private ProcessModelImpl assignedProcessModel;

  MessageFlowImpl() {}

  MessageFlowImpl(final SubjectModelImpl sender, final SubjectModelImpl receiver,
      final StateImpl state, final List<BusinessObjectModelImpl> businessObjectModels) {
    this.sender = sender;
    this.receiver = receiver;
    this.state = state;
    this.businessObjectModels = businessObjectModels;
  }

  MessageFlowImpl(final SubjectModelImpl sender, final SubjectModelImpl receiver,
      final StateImpl state, final List<BusinessObjectModelImpl> businessObjectModels,
      final ProcessModelImpl assignedProcessModel) {
    this(sender, receiver, state, businessObjectModels);
    this.assignedProcessModel = assignedProcessModel;
  }

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
  public List<BusinessObjectModel> getBusinessObjectModels() {
    return ImmutableList.copyOf(businessObjectModels);
  }

  @Override
  public Optional<ProcessModel> getAssignedProcessModel() {
    return Optional.ofNullable(assignedProcessModel);
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
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("mfId", mfId)
        .append("sender", sender).append("receiver", receiver).toString();
  }
}
