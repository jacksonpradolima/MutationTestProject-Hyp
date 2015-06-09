package problem;

import java.util.BitSet;

import jmetal.encodings.variable.Binary;
import jmetal.util.PseudoRandom;
import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.impl.AbstractBinaryProblem;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.util.JMetalException;

/**
 *
 * @author Thiago Nascimento
 *
 */
public class MTNSGAIII extends AbstractBinaryProblem implements ConstrainedProblem<BinarySolution> {

    private static final long serialVersionUID = 7488414018042022225L;

    public final MTProblemBase problemBase;

    public void defineJMetalSettings() {
        setNumberOfVariables(1);
        setNumberOfObjectives(2);
        setName("Mutation Test Problem - NSGAIII");
    }

    public MTNSGAIII(int numberOfTestSuite, int numberOfMutants, int[][] coverage) {
        problemBase = new MTProblemBase(numberOfTestSuite, numberOfMutants, coverage);

        defineJMetalSettings();
    }

    public MTNSGAIII(String filename) {
        problemBase = new MTProblemBase(filename);

        defineJMetalSettings();
    }

    @Override
    public void evaluate(BinarySolution solution) {
        //Binary s = (Binary) solution.getDecisionVariables()[0];
        BitSet bitset = solution.getVariableValue(0);

        Binary s = new Binary(problemBase.getNumberOfTestSuite());
        s.bits_ = bitset;

        // Maximize the mutation score
        solution.setObjective(0, problemBase.getMutantionScore(s) * -1);

        // Minimize the number of test cases (Products)
        solution.setObjective(1, problemBase.getTestScore(s));
    }

    @Override
    protected int getBitsPerVariable(int index) {
        if (index != 0) {
            throw new JMetalException("Problem MutationTestProblem4NSGAIII has only a variable. Index = " + index);
        }
        return problemBase.getNumberOfTestSuite();
    }

    @Override
    public void evaluateConstraints(BinarySolution solution) {
        BitSet bitset = solution.getVariableValue(0);

        Binary s = new Binary(problemBase.getNumberOfTestSuite());
        s.bits_ = bitset;

        // Leastwise a test case must be selected
        if (problemBase.getNumberOfSelectedTestSuite(s) == 0) {
            int random = PseudoRandom.randInt(0, s.getNumberOfBits() - 1);
            s.setIth(random, true);
            evaluate(solution);
        }
    }
}