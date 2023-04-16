package org.ecsail.static_calls;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.StringJoiner;
import java.util.regex.Pattern;

public class StringTools {

	public static boolean isValidEmail(String email)
	{
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
				"[a-zA-Z0-9_+&*-]+)*@" +
				"(?:[a-zA-Z0-9-]+\\.)+[a-z" +
				"A-Z]{2,7}$";
		Pattern pat = Pattern.compile(emailRegex);
		if (email == null)
			return false;
		return pat.matcher(email).matches();
	}
	public static String changeEmptyStringToZero(String input) {
		if(input != null) {
			if(input.equals("")) input= "0";
			else {
				if(!isNumeric(input)) input = "0";
			}
		}
		return input;
	}
	
	public static boolean isNumeric(String strNum) {
	    if (strNum == null) {
	        return false;
	    }
	    try {
	        Double.parseDouble(strNum);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}

	public static boolean isBigDecimal(String str) {
		try {
			new BigDecimal(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static String ParseQuery(String query, String[] parameters) {
		String[] slices = query.split("\\?");
		for(int i = 0; i < parameters.length; i++) slices[i] = slices[i].concat(parameters[i]);
		StringJoiner joiner = new StringJoiner("");
		for(int i = 0; i < slices.length; i++) joiner.add(slices[i]);
		return joiner.toString();
	}

	public static String unwrapToString(Object obj) {
		if (obj == null) {
			return "";
		} else if (obj instanceof SimpleStringProperty) {
			SimpleStringProperty stringProperty = (SimpleStringProperty) obj;
			return stringProperty.get() != null ? stringProperty.get() : "";
		} else if (obj instanceof SimpleIntegerProperty) {
			SimpleIntegerProperty intProperty = (SimpleIntegerProperty) obj;
			return Integer.toString(intProperty.get());
		} else if (obj instanceof SimpleBooleanProperty) {
			SimpleBooleanProperty boolProperty = (SimpleBooleanProperty) obj;
			return boolProperty.get() != false ? Boolean.toString(boolProperty.get()) : "";
		} else {
			return obj.toString() != null ? obj.toString() : "";
		}
	}

	public static <T> String returnFieldValueAsString(Field field, T pojo) {
		String result;
		try {
			result = unwrapToString(field.get(pojo));
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		return result;
	}
	
}
