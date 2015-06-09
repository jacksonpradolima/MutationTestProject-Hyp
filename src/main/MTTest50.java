package main;

import algorithm.hhnsgaIII.HHNSGAIII;
import experiment.Parameters;
import experiment.Settings;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jmetal.util.JMException;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.archive.impl.NonDominatedSolutionListArchive;
import org.uma.jmetal.util.fileoutput.SolutionSetOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

import problem.MTNSGAIII;
import util.ExperimentUtil;

//experimental class for jmetal 5.0
public class MTTest50 {

    public static void main(String[] args) throws JMetalException, JMException, IOException {

        Parameters mutationParameters = ExperimentUtil.verifyParameters(args);

        //print parameters
        mutationParameters.PrintParameters();

        //select problem
        Problem problem = new MTNSGAIII(mutationParameters.getInstance());

        Algorithm algorithm = ExperimentUtil.algorithmBuilder(mutationParameters, problem, mutationParameters.getCrossoverOperator(), mutationParameters.getMutationOperator(), Settings.HEURISTIC_FUNCTION);

        //NonDominatedSolutionList nonDominatedSolutions = new NonDominatedSolutionList();
        NonDominatedSolutionListArchive nonDominatedSolutions = new NonDominatedSolutionListArchive();
        String path = String.format("experiment/%s/%s/%s/%s-%s", ExperimentUtil.getInstanceName(mutationParameters.getInstance()), mutationParameters.getAlgo(), Settings.HEURISTIC_FUNCTION, mutationParameters.getPopulationSize(), mutationParameters.getGenerations());
        List<Integer> numberOfTimesAppliedAllRuns = new ArrayList<>();
        FileWriter fileWriter = new FileWriter(path + "/HHResults");


        /* Execute the Algorithm */
        for (int i = 0; i < mutationParameters.getExecutions(); i++) {
            NonDominatedSolutionListArchive actualNonDominatedSolutions = new NonDominatedSolutionListArchive();

            System.out.println("Run: " + i);

            AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();
            List<Solution> population = ((HHNSGAIII) algorithm).getResult();
            long computingTime = algorithmRunner.getComputingTime();
            System.out.println("Total time of execution: " + computingTime);

            /* Log messages */
            String pathFun = String.format("%s/FUN_%s", path, i);
            String pathVar = String.format("%s/VAR_%s", path, i);

            for (Solution solution : population) {
                nonDominatedSolutions.add(solution);
                actualNonDominatedSolutions.add(solution);
            }

            ExperimentUtil.removeRepeated(actualNonDominatedSolutions);
            System.out.println("Variables values have been writen to file " + pathVar);
            System.out.println("Objectives values have been writen to file " + pathFun);
            new SolutionSetOutput.Printer(actualNonDominatedSolutions.getSolutionList())
                    .setSeparator("\t")
                    .setVarFileOutputContext(new DefaultFileOutputContext(pathVar))
                    .setFunFileOutputContext(new DefaultFileOutputContext(pathFun))
                    .print();

            ExperimentUtil.printSingleHeuristicInformation(fileWriter, i, (HHNSGAIII) algorithm, numberOfTimesAppliedAllRuns);
        }

        ExperimentUtil.printAllHeuristicsInformation(fileWriter, numberOfTimesAppliedAllRuns);
        fileWriter.close();

        ExperimentUtil.printFinalSolutions(nonDominatedSolutions, mutationParameters);
    }
}
