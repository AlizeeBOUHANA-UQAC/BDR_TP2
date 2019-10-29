import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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
}
