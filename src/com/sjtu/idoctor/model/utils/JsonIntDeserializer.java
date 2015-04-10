package com.sjtu.idoctor.model.utils;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class JsonIntDeserializer implements JsonDeserializer<Integer>{

	@Override
	public Integer deserialize(JsonElement json, Type T,
			JsonDeserializationContext context) throws JsonParseException {
		String s = json.getAsJsonPrimitive().getAsString();
		try{
			return Integer.valueOf(s);
		}catch(Exception e){
			return null;
		}
	}

}
