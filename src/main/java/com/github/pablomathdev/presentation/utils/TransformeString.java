package com.github.pablomathdev.presentation.utils;

public class TransformeString {

	public static String tranform(String string) {
		
		for (int i = 0; i < string.length(); i++) {
			if (string.charAt(i) == '-') {

				string = string.replace("-", " ");
			}
		}
		return string;
	}
}
