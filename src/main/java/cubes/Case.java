package cubes;

import java.util.List;

public interface Case {
    String getPattern();

    String getId();

    List<Alg> getProvidedAlgs();

}
