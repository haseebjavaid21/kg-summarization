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
		
		//KgsmrstnController kc = new KgsmrstnController();
		//kc.getSummarizedInfoOfAnEntity("entity", "You Better Run", 10, "FRQ");
		//kc.getSummarizedInfoOfAnEntity("entity", "Lucy Ward (musician)", 10, "FRQ");
		if(directoryCreated){
			KgsmrstnController kc = new KgsmrstnController();
			kc.getSummarizedInfoOfAnEntity("entity", "3WAY FM", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Adrian Griffin", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Andrew Kippis", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Anthony Beaumont-Dark", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Dallas Keuchel", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "E. K. Mawlong", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Finn Schiander", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Hagar Wilde", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Ludwigsburg University", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Roderick Carr", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Swiss Cup", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Kor Royal Cup", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "2011 League of Ireland Cup Final", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "2011 Sparta Prague Open", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "2012–13 UEFA Champions League", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Battle of Bregalnica", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Battle of Rottofreddo", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Burgery ambush", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Massacre on 34th Street", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Triathlon at the 2000 Summer Olympics – Men's", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Akalwadi", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Chitita", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Kuleh Bayan", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Phong Thạnh Tây", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Reamer Barn", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Richmond–Petersburg Turnpike", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Uelsby", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Wehlaberg", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Wernshausen", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Yayoidai Station", 10, "FRQ");
			
			kc.getSummarizedInfoOfAnEntity("entity", "African grey hornbill", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Bornean mountain ground squirrel", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Eastern Sumatran rhinoceros", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Enallagma truncatum", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Lepiota helveola", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Lygodium microphyllum", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Ovophis", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Rubus arizonensis", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Siamese mud carp", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Trachelipus dimorphus", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Foppt den Dämon!", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Hey Boy (Teddybears song)", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "King of the Mountain (film)", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Our Leading Citizen (1939 film)", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Simon (2004 film)", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Sketchy EP 1", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Sky (Faye Wong album)", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Sting Me", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "The Crowd Snores", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Touch of Death (1961 film)", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Ashot I of Iberia", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Cindy Mackey", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Edmund Smith Conklin", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Fabrice Gautrat", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Hiroshi Mori (writer)", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Momchil Tsvetanov", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Najmadin Shukr Rauf", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Storme Warren", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Svyatoslav Tanasov", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "William Anthony Hughes", 10, "FRQ");
			
			kc.getSummarizedInfoOfAnEntity("entity", "1960 Glover Trophy", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "1967 Italian Grand Prix", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "2008 Copa América – FIFA Futsal", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "2008 Copa del Rey Final", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "2010 Belgian Super Cup", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "2013 Slovak Cup Final", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Battle of Calicut (1502)", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Battle of Cepeda (1820)", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Battle on the Elster", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Operation Hump", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Darreh Dang", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Jalalia, Khyber Pakhtunkhwa", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Kings Ripton", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Kotumachagi", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Muławki", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Pinnacle Mountain (South Carolina)", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Saint-Raphaël, Var", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Sauxillanges", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Stara Bučka", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Zarudcze", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Amphisbaena ridleyi", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Balanites", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Bryotropha plantariella", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Hilarographa excellens", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Inverted repeat-lacking clade", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Melaleuca sheathiana", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Pseudanos trimaculatus", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Stemonoporus laevifolius", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Thaia saprophytica", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Trichoscypha cavalliensis", 10, "FRQ");
			
			kc.getSummarizedInfoOfAnEntity("entity", "392 (album)", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Drama City", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "If (Glasvegas song)", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Intensive Care Medicine (journal)", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "It's Still Rock and Roll to Me", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Politiken", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Rebel Love Song", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Terrorist Threats", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Time (Dave Clark album)", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Wide Awake Drunk", 10, "FRQ");
			
			kc.getSummarizedInfoOfAnEntity("entity", "A. Scott Sloan", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Juhan Muks", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Lucy Ward (musician)", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Pe Maung Tin", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Roque Ceruti", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "2013 Gulf Cup of Nations", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Battle of Sampur", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Battle of Zacatecas (1914)", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Convoy HX 156", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Raid on Griessie", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Ayrovo", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Fleckistock", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Henlow", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "North Haledon, New Jersey", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Tchonoro", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Gerbil mouse", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Gonyosoma hodgsoni", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Lineatriton orchimelas", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Nemapogon nigralbella", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Schistura jarutanini", 10, "FRQ");
			
			kc.getSummarizedInfoOfAnEntity("entity", "Can U Get wit It", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Kaalpurush", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "Silence Is Easy (song)", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "The Honolulu Advertiser", 10, "FRQ");
			kc.getSummarizedInfoOfAnEntity("entity", "You Better Run", 10, "FRQ");
		}
		
		// kc.getSummarizedInfoOfAnEntity("entity", "Brad Pitt",10,"EXC");
		// kc.getSummarizedInfoOfAnEntity("entity", "Brad Pitt",10,"DSC");
	}
}