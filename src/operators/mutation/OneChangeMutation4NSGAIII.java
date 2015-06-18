/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operators.mutation;

import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.impl.GenericBinarySolution;
import org.uma.jmetal.util.binarySet.BinarySet;

/**
 * @author Thiago Nascimento
 * @since 2015-06-17
 */
@SuppressWarnings("rawtypes")
public class OneChangeMutation4NSGAIII implements MutationOperator<BinarySolution> {

    private static final long serialVersionUID = 3140299931308698858L;

	private Double mutationProbability_ = null;

    public OneChangeMutation4NSGAIII(double mutationProbability_) {
        this.mutationProbability_ = mutationProbability_;
    }

    public void doMutation(Double probability, BinarySolution solution) throws JMException {
        if (solution.getClass() == GenericBinarySolution.class) {
            if (PseudoRandom.randDouble() < probability) {

				BinarySet binarySolution = solution.getVariableValue(0);
				int numberOfBits = binarySolution.getBinarySetLength();
				int index = PseudoRandom.randInt(0, numberOfBits - 1);

				if (binarySolution.get(index) == false) {
					binarySolution.set(index, true);
				} else {
					binarySolution.set(index, false);
				}
            }
        } else {
            Configuration.logger_.severe("OneChangeMutation4NSGAIII.doMutation: invalid type. ");
                    //+ "" + solution.getDecisionVariables()[0].getVariableType());

            Class cls = java.lang.String.class;
            String name = cls.getName();
            throw new JMException("Exception in " + name + ".doMutation()");
        }
    }

	@Override
	public BinarySolution execute(BinarySolution solution) {
		try {
			this.doMutation(mutationProbability_, solution);
		} catch (JMException e) {
			e.printStackTrace();
		}

		return solution;
	}
}
