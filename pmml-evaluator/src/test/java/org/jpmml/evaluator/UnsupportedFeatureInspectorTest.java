/*
 * Copyright (c) 2014 Villu Ruusmann
 *
 * This file is part of JPMML-Evaluator
 *
 * JPMML-Evaluator is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JPMML-Evaluator is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with JPMML-Evaluator.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jpmml.evaluator;

import java.util.List;

import org.dmg.pmml.DataDictionary;
import org.dmg.pmml.GeneralRegressionModel;
import org.dmg.pmml.Header;
import org.dmg.pmml.PMML;
import org.jpmml.manager.UnsupportedFeatureException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UnsupportedFeatureInspectorTest {

	@Test
	public void inspect() throws Exception {
		PMML pmml = new PMML(new Header(), new DataDictionary(), "4.2");

		GeneralRegressionModel model = new GeneralRegressionModel()
			.withModelType(GeneralRegressionModel.ModelType.COX_REGRESSION);

		pmml = pmml.withModels(model);

		UnsupportedFeatureInspector inspector = new UnsupportedFeatureInspector();

		pmml.accept(inspector);

		List<UnsupportedFeatureException> exceptions = inspector.getExceptions();

		assertEquals(1, exceptions.size());

		UnsupportedFeatureException exception = exceptions.get(0);

		assertEquals("GeneralRegressionModel@modelType=CoxRegression", exception.getMessage());
	}
}