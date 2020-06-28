package org.dice.kgsmrstn;

import java.io.File;

import org.dice.kgsmrstn.controller.KgsmrstnController;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EntitysmrstnTests {

	@Test
	public void contextLoads() {
	}

	public static void main(String[] args) {

		String directoryPath = "./src/main/resources/webapp/output/";
		File file = new File(directoryPath);
		Boolean directoryCreated = file.mkdir();

		if (directoryCreated) {
			KgsmrstnController kc = new KgsmrstnController();
			// use method getSummarizedInfoOfAnEntity with your entity to get
			// the summarization of it.
			// the following are used to generate summarization for entities for
			// evaluation
			kc.generateTriplesforEvaluation("entity", "3WAY FM", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Adrian Griffin", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Andrew Kippis", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Anthony Beaumont-Dark", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Dallas Keuchel", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "E. K. Mawlong", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Finn Schiander", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Hagar Wilde", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Ludwigsburg University", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Roderick Carr", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Swiss Cup", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Kor Royal Cup", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "2011 League of Ireland Cup Final", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "2011 Sparta Prague Open", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "2012–13 UEFA Champions League", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Battle of Bregalnica", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Battle of Rottofreddo", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Burgery ambush", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Massacre on 34th Street", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Triathlon at the 2000 Summer Olympics – Men's", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Akalwadi", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Chitita", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Kuleh Bayan", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Phong Thạnh Tây", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Reamer Barn", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Richmond–Petersburg Turnpike", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Uelsby", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Wehlaberg", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Wernshausen", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Yayoidai Station", 10, "FRQ");

			kc.generateTriplesforEvaluation("entity", "African grey hornbill", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Bornean mountain ground squirrel", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Eastern Sumatran rhinoceros", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Enallagma truncatum", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Lepiota helveola", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Lygodium microphyllum", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Ovophis", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Rubus arizonensis", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Siamese mud carp", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Trachelipus dimorphus", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Foppt den Dämon!", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Hey Boy (Teddybears song)", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "King of the Mountain (film)", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Our Leading Citizen (1939 film)", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Simon (2004 film)", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Sketchy EP 1", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Sky (Faye Wong album)", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Sting Me", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "The Crowd Snores", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Touch of Death (1961 film)", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Ashot I of Iberia", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Cindy Mackey", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Edmund Smith Conklin", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Fabrice Gautrat", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Hiroshi Mori (writer)", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Momchil Tsvetanov", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Najmadin Shukr Rauf", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Storme Warren", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Svyatoslav Tanasov", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "William Anthony Hughes", 10, "FRQ");

			kc.generateTriplesforEvaluation("entity", "1960 Glover Trophy", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "1967 Italian Grand Prix", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "2008 Copa América – FIFA Futsal", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "2008 Copa del Rey Final", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "2010 Belgian Super Cup", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "2013 Slovak Cup Final", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Battle of Calicut (1502)", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Battle of Cepeda (1820)", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Battle on the Elster", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Operation Hump", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Darreh Dang", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Jalalia, Khyber Pakhtunkhwa", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Kings Ripton", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Kotumachagi", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Muławki", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Pinnacle Mountain (South Carolina)", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Saint-Raphaël, Var", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Sauxillanges", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Stara Bučka", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Zarudcze", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Amphisbaena ridleyi", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Balanites", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Bryotropha plantariella", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Hilarographa excellens", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Inverted repeat-lacking clade", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Melaleuca sheathiana", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Pseudanos trimaculatus", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Stemonoporus laevifolius", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Thaia saprophytica", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Trichoscypha cavalliensis", 10, "FRQ");

			kc.generateTriplesforEvaluation("entity", "392 (album)", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Drama City", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "If (Glasvegas song)", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Intensive Care Medicine (journal)", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "It's Still Rock and Roll to Me", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Politiken", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Rebel Love Song", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Terrorist Threats", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Time (Dave Clark album)", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Wide Awake Drunk", 10, "FRQ");

			kc.generateTriplesforEvaluation("entity", "A. Scott Sloan", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Juhan Muks", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Lucy Ward (musician)", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Pe Maung Tin", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Roque Ceruti", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "2013 Gulf Cup of Nations", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Battle of Sampur", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Battle of Zacatecas (1914)", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Convoy HX 156", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Raid on Griessie", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Ayrovo", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Fleckistock", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Henlow", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "North Haledon, New Jersey", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Tchonoro", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Gerbil mouse", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Gonyosoma hodgsoni", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Lineatriton orchimelas", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Nemapogon nigralbella", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Schistura jarutanini", 10, "FRQ");

			kc.generateTriplesforEvaluation("entity", "Can U Get wit It", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Kaalpurush", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "Silence Is Easy (song)", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "The Honolulu Advertiser", 10, "FRQ");
			kc.generateTriplesforEvaluation("entity", "You Better Run", 10, "FRQ");
		}
	}
}