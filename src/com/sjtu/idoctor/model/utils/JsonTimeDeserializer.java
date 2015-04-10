package com.sjtu.idoctor.model.utils;

import java.lang.reflect.Type;
import java.sql.Time;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class JsonTimeDeserializer implements JsonDeserializer<Time>{

	@Override
	public Time deserialize(JsonElement json, Type T,
			JsonDeserializationContext context) throws JsonParseException {
		String s = json.getAsJsonPrimitive().getAsString();
		try{
			return Time.valueOf(s);
		}catch(Exception e){
			return null;
		}
	}

}
