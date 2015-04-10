package com.sjtu.idoctor.model.utils;

import java.lang.reflect.Type;
import java.sql.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class JsonSqlDateDeserializer implements JsonDeserializer<Date>{

	@Override
	public Date deserialize(JsonElement json, Type T,
			JsonDeserializationContext arg2) throws JsonParseException {
		String s = json.getAsJsonPrimitive().getAsString();
		try{
			Date sqlDate = Date.valueOf(s);
			return sqlDate;
		}catch(Exception e){
			return null;
		}
	}

}
