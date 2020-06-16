package org.dice.kgsmrstn;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

import org.dice.kgsmrstn.controller.KgsmrstnController;

@SpringBootTest
public class KgsmrstnApplicationTests {

	@Test
	public void contextLoads() {
	}

	public static void main(String[] args) {
		
		String directoryPath = "./src/main/resources/webapp/output/";
		File file = new File(directoryPath);
		Boolean directoryCreated = file.mkdir();
		
		/*KgsmrstnController kc = new KgsmrstnController();
		kc.getSummarizedInfoOfAnEntity("entity", "You Better Run", 10, "DSC");*/
		//kc.forEvaluation("entity", "Lucy Ward (musician)", 10, "DSC");
		if(directoryCreated){
			KgsmrstnController kc = new KgsmrstnController();
			kc.generateTriplesforEvaluation("entity", "3WAY FM", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Adrian Griffin", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Andrew Kippis", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Anthony Beaumont-Dark", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Dallas Keuchel", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "E. K. Mawlong", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Finn Schiander", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Hagar Wilde", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Ludwigsburg University", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Roderick Carr", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Swiss Cup", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Kor Royal Cup", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "2011 League of Ireland Cup Final", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "2011 Sparta Prague Open", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "2012–13 UEFA Champions League", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Battle of Bregalnica", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Battle of Rottofreddo", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Burgery ambush", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Massacre on 34th Street", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Triathlon at the 2000 Summer Olympics – Men's", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Akalwadi", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Chitita", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Kuleh Bayan", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Phong Thạnh Tây", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Reamer Barn", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Richmond–Petersburg Turnpike", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Uelsby", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Wehlaberg", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Wernshausen", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Yayoidai Station", 10, "DSC");
			
			kc.generateTriplesforEvaluation("entity", "African grey hornbill", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Bornean mountain ground squirrel", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Eastern Sumatran rhinoceros", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Enallagma truncatum", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Lepiota helveola", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Lygodium microphyllum", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Ovophis", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Rubus arizonensis", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Siamese mud carp", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Trachelipus dimorphus", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Foppt den Dämon!", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Hey Boy (Teddybears song)", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "King of the Mountain (film)", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Our Leading Citizen (1939 film)", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Simon (2004 film)", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Sketchy EP 1", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Sky (Faye Wong album)", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Sting Me", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "The Crowd Snores", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Touch of Death (1961 film)", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Ashot I of Iberia", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Cindy Mackey", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Edmund Smith Conklin", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Fabrice Gautrat", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Hiroshi Mori (writer)", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Momchil Tsvetanov", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Najmadin Shukr Rauf", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Storme Warren", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Svyatoslav Tanasov", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "William Anthony Hughes", 10, "DSC");
			
			kc.generateTriplesforEvaluation("entity", "1960 Glover Trophy", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "1967 Italian Grand Prix", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "2008 Copa América – FIFA Futsal", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "2008 Copa del Rey Final", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "2010 Belgian Super Cup", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "2013 Slovak Cup Final", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Battle of Calicut (1502)", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Battle of Cepeda (1820)", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Battle on the Elster", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Operation Hump", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Darreh Dang", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Jalalia, Khyber Pakhtunkhwa", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Kings Ripton", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Kotumachagi", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Muławki", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Pinnacle Mountain (South Carolina)", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Saint-Raphaël, Var", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Sauxillanges", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Stara Bučka", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Zarudcze", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Amphisbaena ridleyi", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Balanites", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Bryotropha plantariella", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Hilarographa excellens", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Inverted repeat-lacking clade", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Melaleuca sheathiana", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Pseudanos trimaculatus", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Stemonoporus laevifolius", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Thaia saprophytica", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Trichoscypha cavalliensis", 10, "DSC");
			
			kc.generateTriplesforEvaluation("entity", "392 (album)", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Drama City", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "If (Glasvegas song)", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Intensive Care Medicine (journal)", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "It's Still Rock and Roll to Me", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Politiken", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Rebel Love Song", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Terrorist Threats", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Time (Dave Clark album)", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Wide Awake Drunk", 10, "DSC");
			
			kc.generateTriplesforEvaluation("entity", "A. Scott Sloan", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Juhan Muks", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Lucy Ward (musician)", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Pe Maung Tin", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Roque Ceruti", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "2013 Gulf Cup of Nations", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Battle of Sampur", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Battle of Zacatecas (1914)", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Convoy HX 156", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Raid on Griessie", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Ayrovo", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Fleckistock", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Henlow", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "North Haledon, New Jersey", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Tchonoro", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Gerbil mouse", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Gonyosoma hodgsoni", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Lineatriton orchimelas", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Nemapogon nigralbella", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Schistura jarutanini", 10, "DSC");
			
			kc.generateTriplesforEvaluation("entity", "Can U Get wit It", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Kaalpurush", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "Silence Is Easy (song)", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "The Honolulu Advertiser", 10, "DSC");
			kc.generateTriplesforEvaluation("entity", "You Better Run", 10, "DSC");
		}
		
		// kc.forEvaluation("entity", "Brad Pitt",10,"DSC");
		// kc.forEvaluation("entity", "Brad Pitt",10,"DSC");
	}
}