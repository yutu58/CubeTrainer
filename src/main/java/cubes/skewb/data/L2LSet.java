package cubes.skewb.data;

import java.util.ArrayList;
import java.util.List;

public class L2LSet {
    private int id;
    private String name;
    private List<L2LCase> cases;

    public L2LSet(int id, String name) {
        this.id = id;
        this.name = name;
        this.cases = new ArrayList<>();
    }

    public void addCase(L2LCase l2LCase) {
        this.cases.add(l2LCase);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<L2LCase> getCases() {
        return cases;
    }

    public void setCases(List<L2LCase> cases) {
        this.cases = cases;
    }
}
