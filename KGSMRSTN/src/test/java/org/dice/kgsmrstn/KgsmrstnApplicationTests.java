package org.dice.kgsmrstn;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.dice.kgsmrstn.controller.KgsmrstnController;
import org.dice.kgsmrstn.controller.SummarizationController;

@SpringBootTest
class KgsmrstnApplicationTests {

	@Test
	void contextLoads() {
	}

        public static void main(String[] args) {
		//KgsmrstnController kc = new KgsmrstnController();
                //kc.getKGraph("simple", 0 , 0);
                
                SummarizationController sc = new SummarizationController();
                sc.getBook();
                
	}
}
