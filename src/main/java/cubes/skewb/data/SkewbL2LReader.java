package cubes.skewb.data;

import cubes.Alg;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkewbL2LReader {
    private final Map<String, List<Alg>> providedAlgMap = new HashMap<>();
    private final Map<String, List<Alg>> addedAlgMap = new HashMap<>();

    public List<L2LSet> read() throws IOException {
        try {
            readAlgs();
            List<L2LSet> sets = readSets();
            readCases(sets);
            return sets;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void readAlgs() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("algs/skewb/L2L/providedAlgs.txt"));
        String line;

        while ((line = reader.readLine()) != null){
           String[] parts = line.split(",");
           String key = parts[0];
           String alg = parts[1];
           int timesTimed = Integer.parseInt(parts[2]);
           int average = Integer.parseInt(parts[3]);

           if (!providedAlgMap.containsKey(key)) {
               providedAlgMap.put(key, new ArrayList<Alg>());
           }
           providedAlgMap.get(key).add(new Alg(alg, timesTimed, average));
        }
        reader.close();
    }

    private List<L2LSet> readSets() throws IOException{
        List<L2LSet> res = new ArrayList<>();
        //Maybe use JSON instead?
        InputStream in = getClass().getClassLoader().getResourceAsStream("cubes/skewb/cases/L2LSets.txt");
        if (in == null) {
            throw new IOException();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String line;

        while ((line = reader.readLine()) != null){
            String[] parts = line.split(",");
            L2LSet l2LSet = new L2LSet(Integer.parseInt(parts[0]), parts[1]);
            res.add(l2LSet);
        }
        reader.close();
        return res;
    }

    private void readCases(List<L2LSet> sets) throws IOException{
        //Maybe use JSON instead?
        InputStream in = getClass().getClassLoader().getResourceAsStream("cubes/skewb/cases/L2LCases.txt");
        if (in == null) {
            throw new IOException();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String line;

        while ((line = reader.readLine()) != null){
            String[] parts = line.split(",");
            if (parts.length == 1) {
                continue;
            }
            L2LCase l2LCase = new L2LCase(parts[0], parts[1]);
            if (providedAlgMap.containsKey(l2LCase.getId())) {
                l2LCase.setProvidedAlgs(providedAlgMap.get(l2LCase.getId()));
            }
            sets.get(l2LCase.getSetNumber()-1).addCase(l2LCase);
        }
        reader.close();
    }
}
