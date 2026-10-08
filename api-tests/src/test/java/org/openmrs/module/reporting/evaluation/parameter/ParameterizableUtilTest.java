/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.reporting.evaluation.parameter;

import org.junit.jupiter.api.Test;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.ConditionalParameterCohortDefinition;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParameterizableUtilTest {

	@Test
	public void getMappedType_shouldReturnTheTypeOfAMappedProperty() {
		assertEquals(CohortDefinition.class,
		    ParameterizableUtil.getMappedType(ConditionalParameterCohortDefinition.class, "defaultCohortDefinition"));
	}

	@Test
	public void getMappedType_shouldReturnTheTypeOfAMapOfMappedProperties() {
		assertEquals(CohortDefinition.class, ParameterizableUtil.getMappedType(CompositionCohortDefinition.class, "searches"));
	}

	@Test
	public void getMappedType_shouldReturnNullForAnEmptyProperty() {
		assertNull(ParameterizableUtil.getMappedType(CompositionCohortDefinition.class, ""));
	}

	@Test
	public void getMappedType_shouldNameAPropertyThatDoesNotExist() {
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
		    () -> ParameterizableUtil.getMappedType(CompositionCohortDefinition.class, "noSuchProperty"));
		assertTrue(e.getMessage().contains("noSuchProperty, no such property"), e.getMessage());
	}
}
