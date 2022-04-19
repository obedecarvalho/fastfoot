package com.fastfoot.service.util;

import java.util.Collection;

public class ValidatorUtil {

	public static boolean isEmpty(Object obj) {
		if (obj instanceof Collection<?>) {
			isEmpty((Collection<?>) obj);
		}
		return obj == null;
	}
	
	public static boolean isEmpty(Collection<?> objs) {
		return objs == null || objs.isEmpty();
	}
}
