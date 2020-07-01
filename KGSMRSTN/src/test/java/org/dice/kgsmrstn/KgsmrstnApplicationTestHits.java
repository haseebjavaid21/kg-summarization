package org.dice.kgsmrstn;

import org.junit.Test;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.dice.kgsmrstn.controller.KgsmrstnController;

@SpringBootTest
class KgsmrstnApplicationTestHits {

	@Test
	void contextLoads() {
	}

	public static void main(String[] args) {

		try {
			Path file_path = Paths.get("input_hits.ttl");
			String file_name = "input_hits.ttl";
			String original_fileName = "input_hits.ttl";
			String filecontentType = "Turtle";
			byte[] file_content = null;
			try {
				file_content = Files.readAllBytes(file_path);
			} catch (final IOException e) {
			}
			MultipartFile inputFile = new MockMultipartFile(file_name, original_fileName, filecontentType, file_content);
			
			KgsmrstnController kc = new KgsmrstnController();
			kc.getKGraphHITS(inputFile);

		}

		catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getStackTrace());
		}
	}
}