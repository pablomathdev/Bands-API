package com.github.pablomathdev;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonFileReader {

	public static String readJsonFile(String relativePath) {
		try {
			String absolutePath = new File(relativePath).getAbsolutePath();
			ObjectMapper objectMapper = new ObjectMapper();
			File file = new File(absolutePath);
			return objectMapper.readTree(file).toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
