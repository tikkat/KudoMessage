package se.kudomessage.torsken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import javax.enterprise.context.Dependent;

@Dependent
public class MessageUtilities implements Serializable {
    
    private static ArrayList<String> substantiv = new ArrayList<String>();
    private static ArrayList<String> oSubstantiv = new ArrayList<String>();
    private static ArrayList<String> adjektiv = new ArrayList<String>();
    private static ArrayList<String> verb = new ArrayList<String>();
    
    private static Random r = new Random();
    
    private static boolean isPopulated = false;
    
    public static String generateRandomMessage() {
        if (!isPopulated)
            populateArrays();
        
        String tmp = "";
        tmp = substantiv.get(r.nextInt(substantiv.size()));
        tmp = tmp + " " + verb.get(r.nextInt(verb.size()));
        tmp = tmp + " " + adjektiv.get(r.nextInt(adjektiv.size()));
        tmp = tmp + " " + oSubstantiv.get(r.nextInt(oSubstantiv.size()));

        return tmp;
    }
    
    private static void populateArrays() {
        isPopulated = true;
        
        substantiv.add("Djuret");
        substantiv.add("Pokerspelaren");
        substantiv.add("Ryggsäcken");
        substantiv.add("Polisinpektören");
        substantiv.add("Sekreteraren");
        substantiv.add("Kasinot");
        substantiv.add("Den snygga tjejen");
        substantiv.add("Den gröna högtalaren");
        substantiv.add("Trummisen från Metall Pojkarna");
        substantiv.add("Taxichauffören");
        substantiv.add("Den blåa vitsippan");
        substantiv.add("Flamman");
        substantiv.add("Stenen");
        substantiv.add("Fåret");
        substantiv.add("Regeringen");
        substantiv.add("Punkten");
        substantiv.add("Avfallet");
        substantiv.add("Den långsamma flugan");
        substantiv.add("Bollen");
        substantiv.add("Mobiltelefonen");
        substantiv.add("Bönan");

        oSubstantiv.add("potatis");
        oSubstantiv.add("kungshamnsbo");
        oSubstantiv.add("stockholmare");
        oSubstantiv.add("ferarri");
        oSubstantiv.add("dator");
        oSubstantiv.add("älg");
        oSubstantiv.add("skalbagge");
        oSubstantiv.add("orangutang");
        oSubstantiv.add("giraff");
        oSubstantiv.add("vodka flaska");
        oSubstantiv.add("färgskrivare");
        oSubstantiv.add("pärm");
        oSubstantiv.add("fotboll");
        oSubstantiv.add("göteborgare");

        verb.add("plockade");
        verb.add("besteg");
        verb.add("sköt");
        verb.add("hoppade på");
        verb.add("rullade");
        verb.add("spelade");
        verb.add("knuffade");
        verb.add("sög");
        verb.add("petade");
        verb.add("puffade");
        verb.add("förstörde");
        verb.add("rövade bort");
        verb.add("ramlade på");
        verb.add("snubblade på");
        verb.add("sparkade på");
        verb.add("åt");
        
        adjektiv.add("grön");
        adjektiv.add("genomskinlig");
        adjektiv.add("ålderdomlig");
        adjektiv.add("fjäderaktig");
        adjektiv.add("turkos");
        adjektiv.add("megatjock");
        adjektiv.add("flötig");
        adjektiv.add("sexig");
        adjektiv.add("tunn");
        adjektiv.add("fett oval");
        adjektiv.add("känslig");
        adjektiv.add("fuktig");
        adjektiv.add("ostädad");
        adjektiv.add("rik");
        adjektiv.add("fattig");
        adjektiv.add("lättfotad");
        adjektiv.add("snabb");
        adjektiv.add("kletig");
        adjektiv.add("prickig");
        adjektiv.add("läskig");
        adjektiv.add("dreglande");
    }
}
