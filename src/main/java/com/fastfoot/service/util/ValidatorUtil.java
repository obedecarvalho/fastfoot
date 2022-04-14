package com.fastfoot.service.util;

import java.util.List;

public class ValidatorUtil {

	public static boolean isEmpty(Object obj) {
		return obj == null;
	}
	
	public static boolean isEmpty(List<Object> objs) {
		return objs == null || objs.isEmpty();
	}
}
