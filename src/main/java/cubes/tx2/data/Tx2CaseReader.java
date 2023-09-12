package cubes.tx2.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Tx2CaseReader {

    public List<Tx2Set> read() throws IOException {
        try {
            List<Tx2Set> sets = readSets();
            readCases(sets);
            return sets;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Tx2Set> readSets() throws IOException{
        List<Tx2Set> res = new ArrayList<>();
        //Maybe use JSON instead?
        InputStream in = getClass().getClassLoader().getResourceAsStream("cubes/2x2/cases/2x2Sets.txt");
        if (in == null) {
            throw new IOException();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String line;

        while ((line = reader.readLine()) != null){
            String[] parts = line.split(",");
            Tx2Set tx2Set = new Tx2Set(Integer.parseInt(parts[0]), parts[1]);
            res.add(tx2Set);
        }
        reader.close();
        return res;
    }

    private void readCases(List<Tx2Set> sets) throws IOException{
        //Maybe use JSON instead?
        InputStream in = getClass().getClassLoader().getResourceAsStream("cubes/2x2/cases/2x2Cases.txt");
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
            Tx2Case tx2Case = new Tx2Case(parts[0], parts[1]);
//            if (providedAlgMap.containsKey(l2LCase.getId())) {
//                l2LCase.setProvidedAlgs(providedAlgMap.get(l2LCase.getId()));
//            }
            sets.get(tx2Case.getSetNumber()-1).addCase(tx2Case);
        }
        reader.close();
    }
}
