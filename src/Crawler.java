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

    public static Spell crawlSpell(String link) throws IOException {
        Document doc = Jsoup.connect(link).get();
        //System.out.println(link);

        return null;
    }

    public static ArrayList<Spell> crawlOutsiders(String link) throws IOException {
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

        ArrayList<Spell> spellsArray = new ArrayList<>();

        for(String u : spellsList) {
            spellsArray.add(crawlSpell(u));
        }

        return spellsArray;
    }

    public static void main(String[] args) throws IOException {
        Document docHome = Jsoup.connect("https://www.d20pfsrd.com/bestiary/Monster-listings").get();
        Elements urlOutsider = docHome.select("td:contains(Outsiders) li.page.new.parent a");
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

            //String name =
            HashMap<String, ArrayList<Spell>> spellsByCrea = new HashMap<>();

            try {
                spellsByCrea.put(url, crawlOutsiders(url)); //envoi au crawler
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


}

