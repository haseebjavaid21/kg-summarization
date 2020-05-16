package org.dice.kgsmrstn;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.dice.kgsmrstn.controller.KgsmrstnController;

@SpringBootTest
public class KgsmrstnApplicationTests {

	@Test
	public void contextLoads() {
	}

        public static void main(String[] args) {
		KgsmrstnController kc = new KgsmrstnController();
                kc.getSummarizedInfoOfAnEntity("entity", "Brad Pitt",10);
	}
}