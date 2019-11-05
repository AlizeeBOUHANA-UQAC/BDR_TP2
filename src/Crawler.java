import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.json.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.regex.*;

import java.io.IOException;
import java.util.HashSet;

public class Crawler {

    public static Spell crawlSpells(String link) throws IOException {

        Document doc = Jsoup.connect(link).get();
        Elements spellsPrepared = doc.select("p:contains(Spells Prepared) + p, p:contains(Spell-Like Abilities) + p"); //selection p suivant <p>Spells Prepared ou <p>Spells Ability


        ArrayList<String> spellsList = new ArrayList<>(); //TODO: doublons possibles !!!
        Elements spells = spellsPrepared.select("a.spell");
        for (Element spell : spells){
            String url = spell.toString();
            url = url.substring(url.indexOf("href")+6);
            url = url.substring(0, url.indexOf("\""));
            spellsList.add(url);
        }

        System.out.print((!spellsList.isEmpty())?"creature " + link + "\n": "");

        //return erreur (spell vide)
        return new Spell(null, 0, new ArrayList<>(), false);
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

            Spell s = crawlSpells(url); //envoi au crawler
        }



/*
        String link = "https://www.d20pfsrd.com/bestiary/monster-listings/outsiders/azata/bralani/";
        Spell s = crawlSpells(link);
        */

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

