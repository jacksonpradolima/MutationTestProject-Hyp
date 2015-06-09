/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problem;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.encodings.solutionType.BinarySolutionType;
import jmetal.encodings.variable.Binary;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

/**
 *
 * @author thaina
 *
 */
/**
 * Class representing problem MutationTest. The problem consist of, given a set
 * of test suites, maximizing it coverage and minimizing the number of test
 * suites.
 */
public class MTProblem extends Problem {

    public final MTProblemBase problemBase;

    public void defineJMetalSettings() {
        numberOfObjectives_ = 2;
        numberOfConstraints_ = 0;
        numberOfVariables_ = 1;
        length_ = new int[numberOfVariables_];
        length_[0] = problemBase.getNumberOfTestSuite();
        problemName_ = "Mutant Test Problem";
        solutionType_ = new BinarySolutionType(this);
    }

    public MTProblem(int numberOfTestSuite, int numberOfMutants, int[][] coverage) {
        problemBase = new MTProblemBase(numberOfTestSuite, numberOfMutants, coverage);
        defineJMetalSettings();
    }

    public MTProblem(String filename) {
        problemBase = new MTProblemBase(filename);
        defineJMetalSettings();
    }

    @Override
    public void evaluate(Solution solution) throws JMException {
        //variable = vector positions
        Binary s = (Binary) solution.getDecisionVariables()[0];

        // Maximize the mutation score
        solution.setObjective(0, problemBase.getMutantionScore(s) * -1);

        // Minimize the number of test cases (Products)
        solution.setObjective(1, problemBase.getTestScore(s));
    }

    @Override
    public void evaluateConstraints(Solution solution) throws JMException {
        Binary s = (Binary) solution.getDecisionVariables()[0];

        // Leastwise a test case must be selected
        if (problemBase.getNumberOfSelectedTestSuite(s) == 0) {
            int random = PseudoRandom.randInt(0, s.getNumberOfBits() - 1);
            s.setIth(random, true);
            evaluate(solution);
        }
    }
}
