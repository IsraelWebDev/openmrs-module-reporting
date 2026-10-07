/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.reporting.web.datasets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.openmrs.module.reporting.indicator.service.IndicatorService;
import org.openmrs.module.reporting.web.RequestParamTestUtil;
import org.openmrs.test.jupiter.BaseContextMockTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CohortIndicatorAndDimensionDataSetEditorTest extends BaseContextMockTest {

	@Mock
	private IndicatorService indicatorService;

	private final CohortIndicatorAndDimensionDataSetEditor editor = new CohortIndicatorAndDimensionDataSetEditor();

	@BeforeEach
	public void setServices() {
		contextMockHelper.setService(IndicatorService.class, indicatorService);
	}

	/**
	 * REPORT-920: adding a new specification submits an empty index.
	 */
	@Test
	public void addSpecification_shouldBindAnEmptyIndexToNull() throws Exception {
		assertNull(RequestParamTestUtil.resolve(
		    RequestParamTestUtil.method(CohortIndicatorAndDimensionDataSetEditor.class, "addSpecification"), "index", "",
		    editor::initBinder));
	}

	@Test
	public void addSpecification_shouldBindAGivenIndex() throws Exception {
		assertEquals(2, RequestParamTestUtil.resolve(
		    RequestParamTestUtil.method(CohortIndicatorAndDimensionDataSetEditor.class, "addSpecification"), "index", "2",
		    editor::initBinder));
	}
}
