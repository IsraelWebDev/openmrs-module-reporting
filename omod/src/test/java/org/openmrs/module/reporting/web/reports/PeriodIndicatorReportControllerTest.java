/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.reporting.web.reports;

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.openmrs.module.reporting.cohort.definition.AgeCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.service.CohortDefinitionService;
import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.openmrs.module.reporting.indicator.service.IndicatorService;
import org.openmrs.module.reporting.web.RequestParamTestUtil;
import org.openmrs.test.jupiter.BaseContextMockTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class PeriodIndicatorReportControllerTest extends BaseContextMockTest {

	@Mock
	private IndicatorService indicatorService;

	@Mock
	private CohortDefinitionService cohortDefinitionService;

	private final PeriodIndicatorReportController controller = new PeriodIndicatorReportController();

	private final Method addColumn = RequestParamTestUtil.method(PeriodIndicatorReportController.class, "addColumn");

	@BeforeEach
	public void setServices() {
		contextMockHelper.setService(IndicatorService.class, indicatorService);
		contextMockHelper.setService(CohortDefinitionService.class, cohortDefinitionService);
	}

	/**
	 * REPORT-921: with an existing indicator chosen, the form submits an empty cohort query.
	 */
	@Test
	public void addColumn_shouldBindAnEmptyCohortQueryToNull() throws Exception {
		assertNull(RequestParamTestUtil.resolve(addColumn, "cohortQuery", "", controller::initBinder));
	}

	/**
	 * REPORT-910: with "Create from cohort query" ticked, the form submits an empty indicator.
	 */
	@Test
	public void addColumn_shouldBindAnEmptyIndicatorToNull() throws Exception {
		assertNull(RequestParamTestUtil.resolve(addColumn, "indicator", "", controller::initBinder));
	}

	@Test
	public void addColumn_shouldBindASelectedCohortQuery() throws Exception {
		AgeCohortDefinition cohortQuery = new AgeCohortDefinition();
		when(cohortDefinitionService.getDefinitionByUuid("cq-uuid")).thenReturn(cohortQuery);
		assertSame(cohortQuery, RequestParamTestUtil.resolve(addColumn, "cohortQuery", "cq-uuid", controller::initBinder));
	}

	@Test
	public void addColumn_shouldRequireAnIndicatorWhenNotCreatingFromACohortQuery() {
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
		    () -> controller.addColumn("uuid", null, "1", "name", null, null, null, null));
		assertEquals("An indicator is required", e.getMessage());
	}

	@Test
	public void addColumn_shouldRequireACohortQueryWhenCreatingFromACohortQuery() {
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
		    () -> controller.addColumn("uuid", null, "1", "name", new CohortIndicator(), null, "on", null));
		assertEquals("A cohort query is required to create an indicator from it", e.getMessage());
	}
}
