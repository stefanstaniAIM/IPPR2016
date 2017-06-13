package at.fhjoanneum.ippr.persistence.entities.model.messageflow;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import com.google.common.collect.Lists;

import at.fhjoanneum.ippr.persistence.builder.Builder;
import at.fhjoanneum.ippr.persistence.entities.model.businessobject.BusinessObjectModelImpl;
import at.fhjoanneum.ippr.persistence.entities.model.process.ProcessModelImpl;
import at.fhjoanneum.ippr.persistence.entities.model.state.StateImpl;
import at.fhjoanneum.ippr.persistence.entities.model.subject.SubjectModelImpl;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.BusinessObjectModel;
import at.fhjoanneum.ippr.persistence.objects.model.messageflow.MessageFlow;
import at.fhjoanneum.ippr.persistence.objects.model.process.ProcessModel;
import at.fhjoanneum.ippr.persistence.objects.model.state.State;
import at.fhjoanneum.ippr.persistence.objects.model.subject.SubjectModel;

public class MessageFlowBuilder implements Builder<MessageFlow> {

  private SubjectModelImpl sender;
  private SubjectModelImpl receiver;
  private StateImpl state;
  private final List<BusinessObjectModelImpl> businessObjectModels = Lists.newArrayList();
  private ProcessModelImpl assignedProcessModel;

  public MessageFlowBuilder sender(final SubjectModel sender) {
    checkNotNull(sender);
    checkArgument(sender instanceof SubjectModelImpl);
    this.sender = (SubjectModelImpl) sender;
    return this;
  }

  public MessageFlowBuilder receiver(final SubjectModel receiver) {
    checkNotNull(receiver);
    checkArgument(receiver instanceof SubjectModelImpl);
    this.receiver = (SubjectModelImpl) receiver;
    return this;
  }

  public MessageFlowBuilder state(final State state) {
    checkNotNull(state);
    checkArgument(state instanceof StateImpl);
    this.state = (StateImpl) state;
    return this;
  }

  public MessageFlowBuilder assignBusinessObjectModel(
      final BusinessObjectModel businessObjectModel) {
    checkNotNull(businessObjectModel);
    checkArgument(businessObjectModel instanceof BusinessObjectModelImpl);
    businessObjectModels.add((BusinessObjectModelImpl) businessObjectModel);
    return this;
  }

  public MessageFlowBuilder assignProcessModel(final ProcessModel pm) {
    checkNotNull(pm);
    checkArgument(pm instanceof ProcessModelImpl);
    assignedProcessModel = (ProcessModelImpl) pm;
    return this;
  }

  @Override
  public MessageFlow build() {
    checkNotNull(sender);
    checkNotNull(receiver);
    checkNotNull(state);
    checkArgument(!businessObjectModels.isEmpty());

    return new MessageFlowImpl(sender, receiver, state, businessObjectModels, assignedProcessModel);
  }

}
