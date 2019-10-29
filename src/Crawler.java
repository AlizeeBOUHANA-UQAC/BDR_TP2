import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.json.*;

import java.util.ArrayList;
import java.util.regex.*;

import java.io.IOException;
import java.util.HashSet;

public class Crawler {

    public static Spell crawlSpells(String link){
        try {
            Document doc = Jsoup.connect(link).get();
            Elements DivSpellOffense = doc.select("div.statblock"); //div de toute les stats de la créature
            DivSpellOffense = DivSpellOffense.select("p:contains(OFFENSE) + p"); //slection balise p suivant titre OFFENSE

            System.out.println(DivSpellOffense);

            ArrayList<String> spellsList = new ArrayList<>();
            Elements spells = DivSpellOffense.select("a.spell");
            for (Element spell : spells){
                String url = spell.toString();
                url = url.substring(url.indexOf("href")+6);
                url = url.substring(0, url.indexOf("\""));
                spellsList.add(url.toString());
            }

            for (String url : spellsList) {
                System.out.println(url);
            }








            /*
            Elements spdet = spell.select("p.SPDet"); // div contenant les infos component, level, spellResist

            //name
            String name = spell.select("div.heading p").toString().replaceAll("</*p>", "");

            //components
            ArrayList<String> components = new ArrayList<>();
            String comp = spdet.select("p:matches(Components)").toString();
            comp = comp.substring(comp.indexOf("<b>Components</b>")+17);
            Pattern pattern = Pattern.compile("[A-Z]*(/?[A-Z]+)");
            Matcher matcher = pattern.matcher(comp);
            while(matcher.find()){
                components.add(matcher.group());
            }

            //level
            int level = 0;
            String lev = spdet.select("p:matches(Level)").toString();
            lev = lev.substring(lev.indexOf("<b>Level</b>")+12);
            pattern = Pattern.compile("[0-9]+");
            matcher = pattern.matcher(lev);
            while(matcher.find()){
                int i = Integer.parseInt(matcher.group());
                if(i > level){
                    level = i;
                }
            }

            //spellResist
            boolean spellResist;
            String resist = spdet.select("p:contains(Spell Resistance yes)").toString();
            spellResist = !resist.equals("");


            // Spell
            return new Spell(name, level, components, spellResist);
*/

        } catch (IOException e) {
            System.err.println("For '" + link + "': " + e.getMessage());
        }

        return new Spell(null, 0, new ArrayList<>(), false);
    }

    public static void main(String[] args) {
        //TODO: récuperer liste des créatures du bestiaires avec links
        //https://www.d20pfsrd.com/bestiary/bestiary-alphabetical/bestiary-a-b/
        //https://www.d20pfsrd.com/bestiary/bestiary-alphabetical/bestiary-c-d/
        // ...

        String link = "https://www.d20pfsrd.com/bestiary/monster-listings/outsiders/azata/bralani/";
        Spell s = crawlSpells(link);
/*
        ArrayList<Spell> arraySpells = new ArrayList<>();
        // boucle pour tous les spells
        for (int i = 1 ; i<1501 ; i++) {
        String link = "http://www.dxcontent.com/SDB_SpellBlock.asp?SDBID="+i;
        Spell s = crawlSpells(link);
        if(s.getName() != null){
        arraySpells.add(s);
        }
        }
        SpellsToJson.GenerateJson(arraySpells);


 */
    }


}

