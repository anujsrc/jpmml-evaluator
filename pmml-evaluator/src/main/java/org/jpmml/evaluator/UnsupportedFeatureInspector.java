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

import com.google.common.collect.Lists;
import org.dmg.pmml.ActivationFunctionType;
import org.dmg.pmml.Aggregate;
import org.dmg.pmml.BaselineModel;
import org.dmg.pmml.Categories;
import org.dmg.pmml.CenterFields;
import org.dmg.pmml.ClusteringModel;
import org.dmg.pmml.ContinuousDistribution;
import org.dmg.pmml.ContinuousScoringMethodType;
import org.dmg.pmml.DecisionTree;
import org.dmg.pmml.GaussianDistribution;
import org.dmg.pmml.GeneralRegressionModel;
import org.dmg.pmml.LinkFunctionType;
import org.dmg.pmml.LocalTransformations;
import org.dmg.pmml.Matrix;
import org.dmg.pmml.MiningFunctionType;
import org.dmg.pmml.MiningModel;
import org.dmg.pmml.MissingValueStrategyType;
import org.dmg.pmml.MultipleModelMethodType;
import org.dmg.pmml.NearestNeighborModel;
import org.dmg.pmml.NeuralLayer;
import org.dmg.pmml.NeuralNetwork;
import org.dmg.pmml.NormDiscrete;
import org.dmg.pmml.OutputField;
import org.dmg.pmml.Predictor;
import org.dmg.pmml.Regression;
import org.dmg.pmml.ResultFeatureType;
import org.dmg.pmml.Segmentation;
import org.dmg.pmml.SequenceModel;
import org.dmg.pmml.SupportVectorMachineModel;
import org.dmg.pmml.SvmRepresentationType;
import org.dmg.pmml.TableLocator;
import org.dmg.pmml.TargetValueStat;
import org.dmg.pmml.TextModel;
import org.dmg.pmml.TimeSeriesModel;
import org.dmg.pmml.TreeModel;
import org.dmg.pmml.VisitorAction;
import org.jpmml.manager.UnsupportedFeatureException;
import org.jpmml.model.AbstractSimpleVisitor;

public class UnsupportedFeatureInspector extends AbstractSimpleVisitor {

	private List<UnsupportedFeatureException> exceptions = Lists.newArrayList();


	@Override
	public VisitorAction visit(Aggregate aggregate){
		Aggregate.Function function = aggregate.getFunction();

		switch(function){
			case MULTISET:
				report(new UnsupportedFeatureException(aggregate, function));
				break;
			default:
				break;
		}

		return super.visit(aggregate);
	}

	@Override
	public VisitorAction visit(BaselineModel baselineModel){
		report(new UnsupportedFeatureException(baselineModel));

		return VisitorAction.SKIP;
	}

	@Override
	public VisitorAction visit(CenterFields centerFields){
		report(new UnsupportedFeatureException(centerFields));

		return VisitorAction.SKIP;
	}

	@Override
	public VisitorAction visit(ClusteringModel clusteringModel){
		ClusteringModel.ModelClass modelClass = clusteringModel.getModelClass();

		switch(modelClass){
			case DISTRIBUTION_BASED:
				report(new UnsupportedFeatureException(clusteringModel, modelClass));
				break;
			default:
				break;
		}


		return super.visit(clusteringModel);
	}

	@Override
	public VisitorAction visit(DecisionTree decisionTree){
		report(new UnsupportedFeatureException(decisionTree));

		return VisitorAction.SKIP;
	}

	@Override
	public VisitorAction visit(GeneralRegressionModel generalRegressionModel){
		GeneralRegressionModel.ModelType modelType = generalRegressionModel.getModelType();

		switch(modelType){
			case COX_REGRESSION:
				report(new UnsupportedFeatureException(generalRegressionModel, modelType));

				return VisitorAction.SKIP;
			default:
				break;
		}

		LinkFunctionType linkFunction = generalRegressionModel.getLinkFunction();
		switch(linkFunction){
			case CAUCHIT:
			case NEGBIN:
				report(new UnsupportedFeatureException(generalRegressionModel, linkFunction));
				break;
			default:
				break;
		}

		return super.visit(generalRegressionModel);
	}

	@Override
	public VisitorAction visit(MiningModel miningModel){
		MiningFunctionType miningFunction = miningModel.getFunctionName();

		switch(miningFunction){
			case REGRESSION:
			case CLASSIFICATION:
				Segmentation segmentation = miningModel.getSegmentation();

				MultipleModelMethodType multipleModelMethod = segmentation.getMultipleModelMethod();
				switch(multipleModelMethod){
					case MEDIAN:
						report(new UnsupportedFeatureException(segmentation, multipleModelMethod));
						break;
					default:
						break;
				}
				break;
			default:
				break;
		}

		return super.visit(miningModel);
	}

	@Override
	public VisitorAction visit(NearestNeighborModel nearestNeighborModel){
		ContinuousScoringMethodType continuousScoringMethod = nearestNeighborModel.getContinuousScoringMethod();

		switch(continuousScoringMethod){
			case MEDIAN:
				report(new UnsupportedFeatureException(nearestNeighborModel, continuousScoringMethod));
				break;
			default:
				break;
		}

		return super.visit(nearestNeighborModel);
	}

	@Override
	public VisitorAction visit(NeuralNetwork neuralNetwork){
		ActivationFunctionType activationFunction = neuralNetwork.getActivationFunction();

		switch(activationFunction){
			case RADIAL_BASIS:
				report(new UnsupportedFeatureException(neuralNetwork, activationFunction));
				break;
			default:
				break;
		}

		return super.visit(neuralNetwork);
	}

	@Override
	public VisitorAction visit(NeuralLayer neuralLayer){
		ActivationFunctionType activationFunction = neuralLayer.getActivationFunction();

		switch(activationFunction){
			case RADIAL_BASIS:
				report(new UnsupportedFeatureException(neuralLayer, activationFunction));
				break;
			default:
				break;
		}

		return super.visit(neuralLayer);
	}

	@Override
	public VisitorAction visit(NormDiscrete normDiscrete){
		NormDiscrete.Method method = normDiscrete.getMethod();

		switch(method){
			case THERMOMETER:
				report(new UnsupportedFeatureException(normDiscrete, method));
				break;
			default:
				break;
		}

		return super.visit(normDiscrete);
	}

	@Override
	public VisitorAction visit(OutputField outputField){
		ResultFeatureType resultFeature = outputField.getFeature();

		switch(resultFeature){
			case STANDARD_ERROR:
				report(new UnsupportedFeatureException(outputField, resultFeature));
				break;
			default:
				break;
		}

		return super.visit(outputField);
	}

	@Override
	public VisitorAction visit(Predictor predictor){
		Matrix matrix = predictor.getMatrix();

		if(matrix != null){
			Categories categories = predictor.getCategories();

			if(categories == null){
				report(new UnsupportedFeatureException(predictor));
			}
		}

		return super.visit(predictor);
	}

	@Override
	public VisitorAction visit(Regression regression){
		report(new UnsupportedFeatureException(regression));

		return VisitorAction.SKIP;
	}

	@Override
	public VisitorAction visit(Segmentation segmentation){
		LocalTransformations localTransformations = segmentation.getLocalTransformations();

		if(localTransformations != null){
			report(new UnsupportedFeatureException(localTransformations));
		}

		return super.visit(segmentation);
	}

	@Override
	public VisitorAction visit(SequenceModel sequenceModel){
		report(new UnsupportedFeatureException(sequenceModel));

		return VisitorAction.SKIP;
	}

	@Override
	public VisitorAction visit(SupportVectorMachineModel supportVectorMachineModel){
		SvmRepresentationType svmRepresentation = supportVectorMachineModel.getSvmRepresentation();

		switch(svmRepresentation){
			case COEFFICIENTS:
				report(new UnsupportedFeatureException(supportVectorMachineModel, svmRepresentation));
				break;
			default:
				break;
		}

		return super.visit(supportVectorMachineModel);
	}

	@Override
	public VisitorAction visit(TableLocator tableLocator){
		report(new UnsupportedFeatureException(tableLocator));

		return VisitorAction.SKIP;
	}

	@Override
	public VisitorAction visit(TargetValueStat targetValueStat){
		ContinuousDistribution continuousDistribution = targetValueStat.getContinuousDistribution();

		if(!(continuousDistribution instanceof GaussianDistribution)){
			report(new UnsupportedFeatureException(continuousDistribution));
		}

		return super.visit(targetValueStat);
	}

	@Override
	public VisitorAction visit(TextModel textModel){
		report(new UnsupportedFeatureException(textModel));

		return VisitorAction.SKIP;
	}

	@Override
	public VisitorAction visit(TimeSeriesModel timeSeriesModel){
		report(new UnsupportedFeatureException(timeSeriesModel));

		return VisitorAction.SKIP;
	}

	@Override
	public VisitorAction visit(TreeModel treeModel){
		MissingValueStrategyType missingValueStrategy = treeModel.getMissingValueStrategy();

		switch(missingValueStrategy){
			case AGGREGATE_NODES:
			case WEIGHTED_CONFIDENCE:
				report(new UnsupportedFeatureException(treeModel, missingValueStrategy));
				break;
			default:
				break;
		}

		return super.visit(treeModel);
	}

	private void report(UnsupportedFeatureException exception){
		this.exceptions.add(exception);
	}

	public List<UnsupportedFeatureException> getExceptions(){
		return this.exceptions;
	}
}