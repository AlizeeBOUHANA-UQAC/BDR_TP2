import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.HashMap;

public class SpellsToJson {
    public static void GenerateJson(ArrayList<Spell> spells) {
        try (FileWriter file = new FileWriter("Spells.json")) {
            BufferedWriter out = new BufferedWriter(file);

            int i = 1;
            out.write("[");
            for (Spell sp: spells) {
                out.write(sp.toJSON().toString());
                if(i<spells.toArray().length)
                    out.write(',');
                i++;
            }
            out.write("]");

            out.close();
            System.out.println("Successfully Copied JSON Object to File...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void GenerateJsonSpellsByCrea(HashMap<String, ArrayList<String>> spellByCrea) {

        ArrayList<JSONObject> json = new ArrayList<>();
        for(String key : spellByCrea.keySet()){
            JSONObject crea = new JSONObject();
            crea.put("nameCreature", key);
            crea.put("spells", spellByCrea.get(key));
            json.add(crea);
        }

        try (FileWriter file = new FileWriter("resources/Spells.json")) {
            BufferedWriter out = new BufferedWriter(file);
            out.write(json.toString());
            out.close();

            System.out.println("Successfully Copied JSON Object to File...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
