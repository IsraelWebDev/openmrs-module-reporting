/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.reporting.web;

import java.lang.reflect.Method;
import java.util.function.Consumer;

import org.springframework.core.annotation.SynthesizingMethodParameter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.DefaultDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Resolves a single {@link RequestParam} argument of a controller method the way Spring MVC does, so tests can check
 * how a form value is bound without starting a web application context.
 */
public class RequestParamTestUtil {

	/**
	 * @param method the controller method
	 * @param paramName the request parameter name, as given in its {@link RequestParam}
	 * @param value the submitted value
	 * @param initBinder registers editors, typically the controller's own {@code @InitBinder} method
	 * @return the resolved argument
	 */
	public static Object resolve(Method method, String paramName, String value, Consumer<WebDataBinder> initBinder)
	        throws Exception {
		int index = -1;
		for (int i = 0; i < method.getParameterCount(); i++) {
			RequestParam rp = method.getParameters()[i].getAnnotation(RequestParam.class);
			if (rp != null && (paramName.equals(rp.value()) || paramName.equals(rp.name()))) {
				index = i;
			}
		}
		if (index < 0) {
			throw new IllegalArgumentException("No @RequestParam(\"" + paramName + "\") on " + method);
		}
		NativeWebRequest request = mock(NativeWebRequest.class);
		when(request.getParameterValues(paramName)).thenReturn(new String[] { value });
		DefaultDataBinderFactory binderFactory = new DefaultDataBinderFactory(binder -> initBinder.accept(binder));
		return new RequestParamMethodArgumentResolver(null, false).resolveArgument(
		    new SynthesizingMethodParameter(method, index), new ModelAndViewContainer(), request, binderFactory);
	}

	/**
	 * @return the public method with the given name
	 */
	public static Method method(Class<?> type, String name) {
		for (Method m : type.getMethods()) {
			if (m.getName().equals(name)) {
				return m;
			}
		}
		throw new IllegalArgumentException("No method " + name + " on " + type);
	}
}
