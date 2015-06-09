package algorithm.hhnsgaIII;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jmetal.util.JMException;
import lowlevelheuristic.HeuristicFunctionType;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.fileoutput.SolutionSetOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import problem.MTNSGAIII;
import util.ExperimentUtil;
import util.HyperHeuristicUtilJM5;

@SuppressWarnings("rawtypes")
public class HHNSGAIII_main {

    protected static String[] crossovers = {
        "SinglePointCrossover",
        "UniformCrossoverBinary4NSGAIII",
        "TwoPointsCrossoverBinary4NSGAIII"
    };

    protected static String[] mutations = {
        "BitFlipMutation",
        "SwapMutationBinary4NSGAIII",
        null
    };

    public static void main(String[] args) throws JMException, IOException {

        List<Integer> numberOfTimesAppliedAllRuns = new ArrayList<>();
        FileWriter fileWriter = new FileWriter("results/HHResults");

        Problem problem = new MTNSGAIII("instances/guizzo_cas.txt");

        Algorithm algorithm = new HHNSGAIIIBuilder(problem)
                .setPopulationSize(200)
                .setSelectionOperator(new BinaryTournamentSelection())
                .setLowLevelHeuristic(HyperHeuristicUtilJM5.getLowLevelHeuristics(crossovers, mutations))
                .setHeuristicFunction(HeuristicFunctionType.ChoiceFunction)
                .setMaxEvaluations(200 * 600)
                .setDivisions(100)
                .build();

        AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

        List<Solution> population = ((HHNSGAIII) algorithm).getResult();
        long computingTime = algorithmRunner.getComputingTime();

        new SolutionSetOutput.Printer(population)
                .setSeparator("\t")
                .setVarFileOutputContext(new DefaultFileOutputContext("results/VAR_HHNSGAIII"))
                .setFunFileOutputContext(new DefaultFileOutputContext("results/FUN_HHNSGAIII"))
                .print();

        ExperimentUtil.printSingleHeuristicInformation(fileWriter, 0, (HHNSGAIII) algorithm, numberOfTimesAppliedAllRuns);
        fileWriter.close();

        JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
        JMetalLogger.logger.info("Objectives values have been written to file FUN_HHNSGAIII");
        JMetalLogger.logger.info("Variables values have been written to file VAR_HHNSGAIII");
    }
}
