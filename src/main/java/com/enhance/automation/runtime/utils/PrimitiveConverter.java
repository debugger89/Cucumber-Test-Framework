package com.enhance.automation.runtime.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * A generic converter utility for converting String values to primitive types.
 * 
 * @author cdushmantha
 *
 */
public class PrimitiveConverter {

	private static final Map<Class<?>, Function<String, ?>> PARSERS = new HashMap<>();

	static {
		PARSERS.put(Long.class, Long::parseLong);
		PARSERS.put(Integer.class, Integer::parseInt);
		PARSERS.put(String.class, String::toString);
		PARSERS.put(Double.class, Double::parseDouble);
		PARSERS.put(Float.class, Float::parseFloat);
		// add other types here.
	}

	@SuppressWarnings("unchecked")
	public static <T> T parse(Class<T> klass, String value) {
		// add some null-handling logic here? and empty values.
		return (T) PARSERS.get(klass).apply(value);
	}

}
