package br.edu.ucv.estagio.util.string;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class StringUtil {
	
	public static String trimAll(String value){
		String string = null;
		
		if (StringUtils.isNotEmpty(value)) {
			string = value.toUpperCase().trim();
			
			while (string.contains("  ")) {
				string = string.replaceAll("  ", " ");
			}
		}
			
		return string;
	}
	
	public static String encoder(String value){
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.encode(value);
	}
}