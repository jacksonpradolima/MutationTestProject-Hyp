package algorithm.hhnsgaIII;

import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.NSGAIII;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.NSGAIIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.SinglePointCrossover;
import org.uma.jmetal.operator.impl.mutation.BitFlipMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.fileoutput.SolutionSetOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

import problem.MTNSGAIII;

public class NSGAIII_main {

	@SuppressWarnings({ "rawtypes"})
	public static void main(String[] args) {
		Problem problem = new MTNSGAIII("instances/guizzo_cas.txt");

		CrossoverOperator crossover = new SinglePointCrossover(0.9);
		MutationOperator mutation = new BitFlipMutation(0.01);
		SelectionOperator selection = new BinaryTournamentSelection();

		Algorithm algorithm = new NSGAIIIBuilder(problem)
			.setPopulationSize(100)
			.setCrossoverOperator(crossover)
			.setMutationOperator(mutation)
			.setSelectionOperator(selection)
			.setMaxEvaluations(100000)
			.setDivisions(100)
			.build();

		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();
		
		List<Solution> population = ((NSGAIII)algorithm).getResult() ;
	    long computingTime = algorithmRunner.getComputingTime() ;

	    new SolutionSetOutput.Printer(population)
	    	.setSeparator("\t")
	        .setVarFileOutputContext(new DefaultFileOutputContext("results/VAR_NSGAIII"))
	        .setFunFileOutputContext(new DefaultFileOutputContext("results/FUN_NSGAIII"))
	        .print();

	    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
	    JMetalLogger.logger.info("Objectives values have been written to file FUN_NSGAIII");
	    JMetalLogger.logger.info("Variables values have been written to file VAR_NSGAIII");
	}
}
