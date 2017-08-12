package at.fhjoanneum.ippr.eventlogger.helper;

import java.util.List;

/**
 * Created by Matthias on 11.08.2017.
 */
public class Arc {
    private String id;
    private String source;
    private String target;
    private List<String> refersToMessagePlaceIds;

    public Arc(String id, String source, String target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public Arc(String id, String source, String target, List<String> refersToMessagePlaceIds) {
        this.id = id;
        this.source = source;
        this.target = target;
        this.refersToMessagePlaceIds = refersToMessagePlaceIds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public List<String> getRefersToMessagePlaceIds() {
        return refersToMessagePlaceIds;
    }

    public void setRefersToMessagePlaceIds(List<String> refersToMessagePlaceIds) {
        this.refersToMessagePlaceIds = refersToMessagePlaceIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Arc arc = (Arc) o;

        if (id != null ? !id.equals(arc.id) : arc.id != null) return false;
        if (source != null ? !source.equals(arc.source) : arc.source != null) return false;
        if (target != null ? !target.equals(arc.target) : arc.target != null) return false;
        return refersToMessagePlaceIds != null ? refersToMessagePlaceIds.equals(arc.refersToMessagePlaceIds) : arc.refersToMessagePlaceIds == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (source != null ? source.hashCode() : 0);
        result = 31 * result + (target != null ? target.hashCode() : 0);
        result = 31 * result + (refersToMessagePlaceIds != null ? refersToMessagePlaceIds.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Arc{" +
                "id='" + id + '\'' +
                ", source='" + source + '\'' +
                ", target='" + target + '\'' +
                ", refersToMessagePlaceIds=" + refersToMessagePlaceIds +
                '}';
    }
}
