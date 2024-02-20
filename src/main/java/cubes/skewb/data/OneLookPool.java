package cubes.skewb.data;

import java.io.Serializable;
import java.util.List;

public class OneLookPool implements Serializable {
    String name;
    List<String> setups;

    public OneLookPool(String name, List<String> setups) {
        this.name = name;
        this.setups = setups;
    }

    public void addSetup(String s) {
        if (!setups.contains(s)) {
            this.setups.add(s);
        }
    }

    public void deleteSetup(String s) {
        this.setups.remove(s);
    }

    public String getName() {
        return name;
    }

    public List<String> getSetups() {
        return setups;
    }

    public void setName(String name) {
        this.name = name;
    }
}
