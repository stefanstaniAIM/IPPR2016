package at.fhjoanneum.ippr.persistence.entities.model.state;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.google.common.base.Objects;

import at.fhjoanneum.ippr.persistence.objects.model.state.RefinementState;

@Embeddable
public class RefinementStateImpl implements RefinementState, Serializable {

  private static final long serialVersionUID = 92021897242630823L;

  @Column
  private String refinementClass;

  RefinementStateImpl() {}

  RefinementStateImpl(final String refinementClass) {
    this.refinementClass = refinementClass;
  }

  @Override
  public String getRefinementClass() {
    return refinementClass;
  }


  @Override
  public int hashCode() {
    return Objects.hashCode(refinementClass);
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final RefinementStateImpl other = (RefinementStateImpl) obj;
    if (refinementClass == null) {
      if (other.refinementClass != null) {
        return false;
      }
    } else if (!refinementClass.equals(other.refinementClass)) {
      return false;
    }
    return true;
  }
}
