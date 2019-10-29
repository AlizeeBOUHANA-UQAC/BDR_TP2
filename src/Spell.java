import org.json.JSONObject;

import java.util.ArrayList;

public class Spell {

    private String name;
    private int level;
    private ArrayList<String> components = new ArrayList<>();
    private boolean spell_resistance;

    public Spell(String _name, int _level, ArrayList<String> _components, boolean _resistance){
        name = _name;
        level = _level;
        components = _components;
        spell_resistance = _resistance;
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();

        obj.put("level", level);
        obj.put("name", name);
        obj.put("components", components);
        obj.put("spell_resistance", spell_resistance);

        return obj;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public ArrayList<String> getComponents() {
        return components;
    }

    public boolean isSpell_resistance() {
        return spell_resistance;
    }
}
