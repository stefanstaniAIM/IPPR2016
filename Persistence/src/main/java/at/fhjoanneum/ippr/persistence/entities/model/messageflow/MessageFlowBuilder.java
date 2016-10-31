package at.fhjoanneum.ippr.persistence.entities.model.messageflow;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import at.fhjoanneum.ippr.persistence.builder.Builder;
import at.fhjoanneum.ippr.persistence.entities.model.state.StateImpl;
import at.fhjoanneum.ippr.persistence.entities.model.subject.SubjectModelImpl;
import at.fhjoanneum.ippr.persistence.objects.model.messageflow.MessageFlow;
import at.fhjoanneum.ippr.persistence.objects.model.state.State;
import at.fhjoanneum.ippr.persistence.objects.model.subject.SubjectModel;

public class MessageFlowBuilder implements Builder<MessageFlow> {

	private SubjectModelImpl sender;
	private SubjectModelImpl receiver;
	private StateImpl state;

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

	@Override
	public MessageFlow build() {
		checkNotNull(sender);
		checkNotNull(receiver);
		checkNotNull(state);

		return new MessageFlowImpl(sender, receiver, state);
	}

}
