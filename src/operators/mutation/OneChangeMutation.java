/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operators.mutation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import jmetal.core.Solution;
import jmetal.encodings.solutionType.BinarySolutionType;
import jmetal.encodings.variable.Binary;
import jmetal.operators.mutation.Mutation;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

/**
 * @author Thiago Nascimento
 * @since 2015-06-17
 */
@SuppressWarnings("rawtypes")
public class OneChangeMutation extends Mutation {

    private static final long serialVersionUID = -8463214133031746709L;
	
	private static final List VALID_TYPES = Arrays.asList(BinarySolutionType.class);

    private Double mutationProbability_ = null;

    public OneChangeMutation(HashMap<String, Object> parameters) {
        super(parameters);
        if (parameters.get("probability") != null) {
            mutationProbability_ = (Double) parameters.get("probability");
        }
    }

    @Override
    public Object execute(Object object) throws JMException {
        Solution solution = (Solution) object;

        if (!VALID_TYPES.contains(solution.getType().getClass())) {
            Configuration.logger_.severe("OneChangeMutation.execute: the solution "
                    + "is not of the right type. The type should be 'Binary', but " + solution.getType() + " is obtained");

            Class cls = java.lang.String.class;
            String name = cls.getName();
            throw new JMException("Exception in " + name + ".execute()");
        }

        this.doMutation(mutationProbability_, solution);
        return solution;
    }

    public void doMutation(Double probability, Solution solution) throws JMException {
        if (solution.getType().getClass() == BinarySolutionType.class) {
            if (PseudoRandom.randDouble() < probability) {
            	Binary binarySolution = ((Binary) solution.getDecisionVariables()[0]);
            	
				int index = PseudoRandom.randInt(0, binarySolution.getNumberOfBits()-1);
                
				if (binarySolution.getIth(index) == false) {
					binarySolution.setIth(index, true);
				} else {
					binarySolution.setIth(index, false);
				}
            }
        } else {
            Configuration.logger_.severe("SwapMutationBinary.doMutation: invalid type. "
                    + "" + solution.getDecisionVariables()[0].getVariableType());

            Class cls = java.lang.String.class;
            String name = cls.getName();
            throw new JMException("Exception in " + name + ".doMutation()");
        }
    }

}
