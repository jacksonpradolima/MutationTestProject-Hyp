package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.util.JMException;
import lowlevelheuristic.LowLevelHeuristic;

public class HyperHeuristicUtil {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<LowLevelHeuristic> getLowLevelHeuristics(String[] crossovers, String[] mutations, double beta) throws JMException {
		List<LowLevelHeuristic> list = new ArrayList<LowLevelHeuristic>();
		
		int id = 1;

		for (String crossover : crossovers) {
			for (String mutation : mutations) {
				HashMap lowLevelParameters = new HashMap<>();
				
				HashMap param = new HashMap<>();
				param.put("probability", 1.0);
				
				String name = String.format("h%s[%s,%s]", id++, crossover, mutation);
				
				lowLevelParameters.put("name", name);
				lowLevelParameters.put("crossover", CrossoverFactory.getCrossoverOperator(crossover, param));
				lowLevelParameters.put("beta", beta);
				if (mutation != null) {
					lowLevelParameters.put("mutation", MutationFactory .getMutationOperator(mutation, param));
				}
				
//				lowLevelParameters.put("alpha", 1.0);
//				lowLevelParameters.put("beta", 1.0);
//				lowLevelParameters.put("w", 2);
//				lowLevelParameters.put("c", 2.0);
				
				LowLevelHeuristic lowLevelHeuristic = new LowLevelHeuristic(lowLevelParameters);
				
				if (!list.contains(lowLevelHeuristic)) {
					list.add(lowLevelHeuristic);
				}
			}
		}
		
		return list;
	}

	public static void print(List<LowLevelHeuristic> lowLevelHeuristics) {
		for(LowLevelHeuristic lowLevel : lowLevelHeuristics){
			System.out.println(lowLevel);
		}
	}
}
