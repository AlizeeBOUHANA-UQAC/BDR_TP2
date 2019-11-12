import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SpellsToJson {
    public static void GenerateJsonAllSpells(ArrayList<Spell> spells) {
        ArrayList<JSONObject> json = new ArrayList<>();
        for (Spell sp: spells) {
            json.add(sp.toJSON());
        }

        try (FileWriter file = new FileWriter("resources/AllSpells.json")) {
            BufferedWriter out = new BufferedWriter(file);
            out.write(json.toString());
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

        try (FileWriter file = new FileWriter("resources/SpellsByCreature.json")) {
            BufferedWriter out = new BufferedWriter(file);
            out.write(json.toString());
            out.close();

            System.out.println("Successfully Copied JSON Object to File...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
