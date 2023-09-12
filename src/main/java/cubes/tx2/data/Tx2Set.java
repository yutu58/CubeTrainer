package cubes.tx2.data;

import cubes.Case;
import cubes.Set;

import java.util.ArrayList;
import java.util.List;

public class Tx2Set implements Set {
    private int id;
    private String name;
    private List<Case> cases;

    public Tx2Set(int id, String name) {
        this.id = id;
        this.name = name;
        this.cases = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Tx2Set{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cases=" + cases +
                '}';
    }

    public void addCase(Tx2Case tx2Case) {
        this.cases.add(tx2Case);
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

    public List<Case> getCases() {
        return cases;
    }

    public void setCases(List<Case> cases) {
        this.cases = cases;
    }
}
