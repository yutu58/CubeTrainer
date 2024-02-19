package cubes.skewb.data;

import java.io.Serializable;
import java.util.List;

public class AlgPool implements Serializable {
    String name;

    List<Integer> setIds;

    public AlgPool(String name, List<Integer> setIds) {
        this.name = name;
        this.setIds = setIds;
    }

    //ToString?

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getSetIds() {
        return setIds;
    }

    public void setSetIds(List<Integer> setIds) {
        this.setIds = setIds;
    }
}
