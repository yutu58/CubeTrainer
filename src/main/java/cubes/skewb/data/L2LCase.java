package cubes.skewb.data;

import java.util.ArrayList;
import java.util.List;

public class L2LCase {
    private String id;
    private String pattern;
    private List<Alg> providedAlgs;
    private List<Alg> addedAlgs;

    public L2LCase(String id, String pattern) {
        this.id = id;
        this.pattern = pattern;
        this.providedAlgs = new ArrayList<>();
        this.addedAlgs = new ArrayList<>();
    }

    public int getSetNumber() {
        return Integer.parseInt(id.replaceAll("[^0-9]", ""));
    }

    public String getId() {
        return id;
    }

    public String getPattern() {
        return pattern;
    }

    public List<Alg> getProvidedAlgs() {
        return providedAlgs;
    }

    public List<Alg> getAddedAlgs() {
        return addedAlgs;
    }

    public void setProvidedAlgs(List<Alg> providedAlgs) {
        this.providedAlgs = providedAlgs;
    }

    public void setAddedAlgs(List<Alg> addedAlgs) {
        this.addedAlgs = addedAlgs;
    }
}
