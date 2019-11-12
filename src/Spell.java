import org.json.JSONObject;

import java.util.ArrayList;

public class Spell {

    public String name;
    public String school;
    public ArrayList<String> level;
    public ArrayList<String> components;
    public boolean spell_resistance;
    public String description;

    public Spell(String _name, String _school, String _description, ArrayList<String> _level, ArrayList<String> _components, boolean _resistance){
        name = _name;
        school = _school;
        level = _level;
        components = _components;
        spell_resistance = _resistance;
        description = _description;
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();

        obj.put("level", level);
        obj.put("name", name);
        obj.put("components", components);
        obj.put("spell_resistance", spell_resistance);
        obj.put("school", school);
        obj.put("description", description);

        return obj;
    }

    @Override
    public String toString() {
        return "Name : " +
                name + "\n" +
                "School : " +
                school + "\n" +
                "Levels : " +
                level + "\n" +
                "Components : " +
                components + "\n" +
                "Spell Resist : " +
                spell_resistance + "\n" +
                "Description : " +
                description + "\n";
    }
}
