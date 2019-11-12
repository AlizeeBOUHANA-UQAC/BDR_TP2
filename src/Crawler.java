import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.json.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;

import java.io.IOException;
import java.util.HashSet;

public class Crawler {

    public static String crawlNameSpell(String link) throws IOException {
        Document doc = Jsoup.connect(link).get();
        return doc.select("article>h1").text();
    }

    public static Spell crawlSpell(String link) throws IOException {
        Document page = Jsoup.connect(link).get();
        Elements spell = page.select("div.page-center");

        Elements dividers = spell.select("p.divider");
        Elements elem_casting = dividers.select("p:contains(CASTING)");
        Elements elem_effet = dividers.select("p:contains(EFFECT)");
        Elements elem_descr = dividers.select("p:contains(DESCRIPTION)");

        String name = page.select("h1").text();
        String school_level_brut = dividers.select("p:contains(CASTING)").prev().text();
        String component_brut = dividers.select("p:contains(CASTING)").next().text();
        String spell_resist_brut = dividers.select("p:contains(EFFECT)").next().text();
        String description = dividers.select("p:contains(DESCRIPTION)").nextAll("p").text();

        String[] school_level_brut_splitted = school_level_brut.split(";");
        //School
        String school = "";
        if(school_level_brut_splitted.length>0) {
            school = school_level_brut_splitted[0].replaceAll("School ","").split(" ")[0];
        }

        //Level
        ArrayList<String> levels = new ArrayList<>();
        if(school_level_brut_splitted.length>1) {
            String[] level = school_level_brut.split(";")[1].replaceFirst(" *Level *", "").split(", ");
            for (String s : level) {
                levels.add(s.replaceFirst(".*/", "").replaceAll(" ", ":"));
            }
        }

        //Spell Resist
        boolean spell_resist = spell_resist_brut.contains("Spell Resistance yes");

        //Components
        String[] component_brut_splitted = component_brut.split(" Components ");
        ArrayList<String> components = new ArrayList<>();
        if(component_brut_splitted.length>1) {
            String[] _components = component_brut_splitted[1].split(", ");
            for (String s : _components) {
                components.add(s.split(" ")[0]);
            }
        }

        return new Spell(name, school, description, levels, components, spell_resist);
    }

    public static ArrayList<String> crawlOutsiders(String link) throws IOException {
        Document doc = Jsoup.connect(link).get();
        Elements spellsPrepared = doc.select("p:contains(Spells Prepared) + p, p:contains(Spell-Like Abilities) + p"); //selection p suivant <p>Spells Prepared ou <p>Spells Ability

        HashSet<String> spellsList = new HashSet<>(); //pas de doublons dans le Set
        Elements spells = spellsPrepared.select("a.spell");
        for (Element spell : spells){
            String url = spell.toString();
            url = url.substring(url.indexOf("href")+6);
            url = url.substring(0, url.indexOf("\""));
            spellsList.add(url);
        }

        ArrayList<String> spellsArray = new ArrayList<>();

        for(String u : spellsList) {
            spellsArray.add(crawlNameSpell(u));
        }

        return spellsArray;
    }

    public static void CrawlerSpellsByCrea() throws IOException {
        Document docHome = Jsoup.connect("https://www.d20pfsrd.com/bestiary/Monster-listings").get();
        Elements urlOutsider = docHome.select("td:contains(Outsiders) li.page.new.parent a");

        HashMap<String, ArrayList<String>> spellsByCrea = new HashMap<>();

        for(Element outsider : urlOutsider){
            String url = outsider.toString();
            url = url.substring(url.indexOf("\"")+1);
            url = url.substring(0, url.indexOf("\""));

            Document doc = Jsoup.connect(url).get();
            Elements typeUrl = doc.select("h4:contains(Binding)");

            if(!typeUrl.isEmpty()){
                Elements binds = doc.select("div.page-center div.ogn-childpages a[href~=.*bestiary/monster-listings/outsiders/.*]");
                for(Element bind : binds) {
                    url = binds.toString();
                    url = url.substring(url.indexOf("\"") + 1);
                    url = url.substring(0, url.indexOf("\""));
                }
            }

            String[] urlSplitted = url.split("/");
            String name = urlSplitted[urlSplitted.length-1].replaceAll("-", " ");
            name = name.substring(0, 1).toUpperCase() + name.substring(1);

            try {
                ArrayList<String> spellCrea = crawlOutsiders(url);

                if (spellCrea.size() > 0)
                    spellsByCrea.put(name, spellCrea);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        SpellsToJson.GenerateJsonSpellsByCrea(spellsByCrea);
    }

    public static ArrayList<String> GetAllSpellsURL() throws IOException{
        ArrayList<String> links = new ArrayList<>();

        String link = "https://www.d20pfsrd.com/magic/all-spells/";
        String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        int actualLetter = 0;

        while(actualLetter < letters.length) {
            Document docHome = Jsoup.connect(link + letters[actualLetter] + "/").get();
            Elements article = docHome.select("article."+letters[actualLetter]);
            Elements list = article.select("ul").select("a[href~=.*magic/all-spells/.*]");
            for (Element a : list) {
                links.add(a.attr("href"));
            }
            actualLetter++;
        }

        return links;
    }

    public static void CrawlerAllSpells() throws IOException {
        ArrayList<String> urls = GetAllSpellsURL();
        ArrayList<Spell> spells = new ArrayList<>();
        for(String url : urls) {
            try {
                Spell s = crawlSpell(url);
                if(s.school != "" && s.level.size() > 0 && s.components.size() > 0) {
                    spells.add(s);
                }
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        }

        SpellsToJson.GenerateJsonAllSpells(spells);
    }

    public static void main(String[] args){
        //Crawl spells by creature
        try {
            CrawlerSpellsByCrea();
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        //Crawl all spells
        /*try {
            CrawlerAllSpells();
        }
        catch(IOException e) {
            e.printStackTrace();
        }*/
    }
}

