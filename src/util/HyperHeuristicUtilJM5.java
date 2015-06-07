package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lowlevelheuristic.LowLevelHeuristic;
import lowlevelheuristic.LowLevelHeuristicJM5;
import operators.crossover.TwoPointsCrossoverBinary4NSGAIII;
import operators.crossover.UniformCrossoverBinary4NSGAIII;
import operators.mutation.SwapMutationBinary4NSGAIII;

import org.uma.jmetal.operator.impl.crossover.SinglePointCrossover;
import org.uma.jmetal.operator.impl.mutation.BitFlipMutation;

public class HyperHeuristicUtilJM5 {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<LowLevelHeuristic> getLowLevelHeuristics(String[] crossovers, String[] mutations) {
		List<LowLevelHeuristic> list = new ArrayList<LowLevelHeuristic>();
		
		int id = 1;

		for (String crossover : crossovers) {
			for (String mutation : mutations) {
				HashMap lowLevelParameters = new HashMap<>();
				
				HashMap param = new HashMap<>();
				param.put("probability", 1.0);
				
				String name = String.format("h%s[%s,%s]", id++, crossover, mutation);
				
				lowLevelParameters.put("name", name);
				lowLevelParameters.put("crossover", getCrossoverOperator(crossover, param));
				
				if (mutation != null) {
					lowLevelParameters.put("mutation", getMutationOperator(mutation, param));
				}
				
				LowLevelHeuristicJM5 lowLevelHeuristic = new LowLevelHeuristicJM5(lowLevelParameters);
				
				if (!list.contains(lowLevelHeuristic)) {
					list.add(lowLevelHeuristic);
				}
			}
		}
		
		return list;
	}
	
	public static Object getMutationOperator(String name, HashMap parameters) {
		if (name.equalsIgnoreCase("BitFlipMutation")){
			return new BitFlipMutation(1);
		}else if (name.equalsIgnoreCase("SwapMutationBinary4NSGAIII")){
			return new SwapMutationBinary4NSGAIII(1.0);
		}
		return null;
	}
	
	public static Object getCrossoverOperator(String name, HashMap parameters) {
	    if (name.equalsIgnoreCase("SinglePointCrossover")){
	      return new SinglePointCrossover(1.0);
	    }else if (name.equalsIgnoreCase("UniformCrossoverBinary4NSGAIII")){
	    	return new UniformCrossoverBinary4NSGAIII(parameters);
	    }else if (name.equalsIgnoreCase("TwoPointsCrossoverBinary4NSGAIII")){
	    	return new TwoPointsCrossoverBinary4NSGAIII(parameters);
	    }
		return null;
	}
	

	public static void print(List<LowLevelHeuristic> lowLevelHeuristics) {
		for(LowLevelHeuristic lowLevel : lowLevelHeuristics){
			System.out.println(lowLevel);
		}
	}
}
