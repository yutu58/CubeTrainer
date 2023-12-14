package cubes.skewb.optimizers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class LengthRater implements AlgRater {
    @Override
    public List<String> rate(Set<String> input) {
        List<String> sortedList = new ArrayList<>(input);
        sortedList.sort(Comparator.comparingInt((String s) -> s.split(" ").length)
                .thenComparing(Comparator.naturalOrder()));
        return sortedList;
    }
}
