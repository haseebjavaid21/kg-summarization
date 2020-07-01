package org.dice.kgsmrstn;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.dice.kgsmrstn.controller.KgsmrstnController;

@SpringBootTest
class KgsmrstnApplicationTestsSalSa {

	@Test
	void contextLoads() {
	}

	public static void main(String[] args) {

		try {
			Path file_path = Paths.get("input_salsa.ttl");
			String file_name = "input_salsa.ttl";
			String original_fileName = "input_salsa.ttl";
			String filecontentType = "Turtle";
			byte[] file_content = null;
			try {
				file_content = Files.readAllBytes(file_path);
			} catch (final IOException e) {
			}
			MultipartFile inputFile = new MockMultipartFile(file_name, original_fileName, filecontentType, file_content);
			
			KgsmrstnController kc = new KgsmrstnController();
			kc.getKGraphSalsa(inputFile);

		}

		catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getStackTrace());
		}
	}
}