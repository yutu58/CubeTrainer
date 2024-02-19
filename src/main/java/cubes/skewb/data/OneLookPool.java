package cubes.skewb.data;

import java.util.ArrayList;
import java.util.List;

public class OneLookPool {
    String name;
    List<String> setups;

    public OneLookPool(String name, List<String> setups) {
        this.name = name;
        this.setups = setups;
    }

    public OneLookPool() {
        this.name = "--";
        this.setups = new ArrayList<>();
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

    public void setName(String name) {
        this.name = name;
    }
}
