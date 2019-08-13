package com.demo.sensordata.utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

	public static Map<String, String> convertToHashMap(String json) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = null;
		try {
			map = mapper.readValue(json, Map.class);
		} catch (IOException e) {

		}
		return map;

	}

	public static String convertToString(Object map) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(map);
	}

	public static String removeTrailingSlash(List<String> alerts) throws JsonProcessingException {
		return JsonUtil.convertToString(alerts).replaceAll("\\\\", "").replaceAll("\"\\{", "{").replaceAll("\\}\"",
				"}");
	}
}
