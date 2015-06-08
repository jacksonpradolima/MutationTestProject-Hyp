package algorithm;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jmetal.core.Algorithm;
import jmetal.core.Problem;
import jmetal.util.PseudoRandom;
import lowlevelheuristic.LowLevelHeuristic;

public abstract class HyperHeuristic extends Algorithm {

	private static final long serialVersionUID = 7229836683263798932L;

	private List<LowLevelHeuristic> lowLevelHeuristics;
	
	private FileWriter lowLevelHeuristicsRankWriter;
	
    private FileWriter lowLevelHeuristicsTimeWriter;
    
    private FileWriter qDebugWriter;
    
    private FileWriter auxDebugWriter;
    
    private FileWriter nDebugWriter;
    
    private FileWriter rDebugWriter;
    
    private String generationsOutputDirectory;

	public HyperHeuristic(Problem problem) {
		super(problem);
		this.lowLevelHeuristics = new ArrayList<>();
	}

	public LowLevelHeuristic addLowLevelHeuristic(HashMap<String, Object> parameters) {
		LowLevelHeuristic lowLevelHeuristic = new LowLevelHeuristic(parameters);
		if (!lowLevelHeuristics.contains(lowLevelHeuristic)) {
			lowLevelHeuristics.add(lowLevelHeuristic);
			return lowLevelHeuristic;
		} else {
			return null;
		}
	}
	
	public void clearLowLeverHeuristicsValues() {
		LowLevelHeuristic.clearAllStaticValues();
		for (LowLevelHeuristic lowLevelHeuristic : lowLevelHeuristics) {
			lowLevelHeuristic.clearAllValues();
		}
	}

	public List<LowLevelHeuristic> getLowLevelHeuristics() {
		return this.lowLevelHeuristics;
	}

	public int[] getLowLevelHeuristicsNumberOfTimesApplied() {
		int[] allTimesApplied = new int[lowLevelHeuristics.size()];
		for (int i = 0; i < lowLevelHeuristics.size(); i++) {
			LowLevelHeuristic lowLevelHeuristic = lowLevelHeuristics.get(i);
			allTimesApplied[i] = lowLevelHeuristic.getNumberOfTimesApplied();
		}
		return allTimesApplied;
	}

	public int getLowLevelHeuristicsSize() {
		return lowLevelHeuristics.size();
	}

	public void setLowLevelHeuristic(List<LowLevelHeuristic> lowLevelHeuristics) {
		this.lowLevelHeuristics = lowLevelHeuristics;		
	}
	
	public LowLevelHeuristic getApplyingHeuristic(Comparator<LowLevelHeuristic> comparator) {
        List<LowLevelHeuristic> allLowLevelHeuristics = new ArrayList<>(lowLevelHeuristics);
        Collections.sort(allLowLevelHeuristics, comparator);
        List<LowLevelHeuristic> applyingHeuristics = new ArrayList<>();

        //Find the best tied heuristics
        Iterator<LowLevelHeuristic> iterator = allLowLevelHeuristics.iterator();
        LowLevelHeuristic heuristic;
        LowLevelHeuristic nextHeuristic = iterator.next();
        do {
            heuristic = nextHeuristic;
            applyingHeuristics.add(heuristic);
        } while (iterator.hasNext() && comparator.compare(heuristic, nextHeuristic = iterator.next()) == 0);

        return applyingHeuristics.get(PseudoRandom.randInt(0, applyingHeuristics.size() - 1));
    }
	
	public void setDebugPath(String path) throws IOException {
        if (qDebugWriter != null) {
            qDebugWriter.close();
        }
        qDebugWriter = new FileWriter(path + "_q.txt");
        if (auxDebugWriter != null) {
            auxDebugWriter.close();
        }
        auxDebugWriter = new FileWriter(path + "_aux.txt");
        if (rDebugWriter != null) {
            rDebugWriter.close();
        }
        rDebugWriter = new FileWriter(path + "_r.txt");
        if (nDebugWriter != null) {
            nDebugWriter.close();
        }
        nDebugWriter = new FileWriter(path + "_n.txt");
    }
	
	public void setGenerationsOutputDirectory(String path) throws IOException {
        generationsOutputDirectory = path;
    }

    public void printLowLevelHeuristicsInformation(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (LowLevelHeuristic lowLevelHeuristic : lowLevelHeuristics) {
                writer.write("Name: " + lowLevelHeuristic.getName() + ":\n");
                writer.write("\tRank: " + lowLevelHeuristic.getRank() + "\n");
                writer.write("\tElapsed Time: " + lowLevelHeuristic.getElapsedTime() + "\n");
                writer.write("\tChoice Value: " + lowLevelHeuristic.getChoiceFunctionValue() + "\n");
                writer.write("\tNumber of Times Applied: " + lowLevelHeuristic.getNumberOfTimesApplied() + "\n");
                writer.write("\n");
            }
            writer.write("----------------------\n\n");
        } catch (IOException ex) {
            Logger.getLogger(HyperHeuristic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setLowLevelHeuristicsRankPath(String path) throws IOException {
        if (lowLevelHeuristicsRankWriter != null) {
            lowLevelHeuristicsRankWriter.close();
        }
        lowLevelHeuristicsRankWriter = new FileWriter(path);
    }

    public void setLowLevelHeuristicsTimePath(String path) throws IOException {
        if (lowLevelHeuristicsTimeWriter != null) {
            lowLevelHeuristicsTimeWriter.close();
        }
        lowLevelHeuristicsTimeWriter = new FileWriter(path);
    }
    
    public void debug(){
    	if (qDebugWriter != null) {
            try {
                for (LowLevelHeuristic lowLevelHeuristic : lowLevelHeuristics) {
                    qDebugWriter.write(lowLevelHeuristic.getQ() + "\t");
                }
                qDebugWriter.write("\n");
            } catch (IOException ex) {
                Logger.getLogger(HyperHeuristic.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (auxDebugWriter != null) {
            try {
                for (LowLevelHeuristic lowLevelHeuristic : lowLevelHeuristics) {
                    auxDebugWriter.write(lowLevelHeuristic.getAux() + "\t");
                }
                auxDebugWriter.write("\n");
            } catch (IOException ex) {
                Logger.getLogger(HyperHeuristic.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (rDebugWriter != null) {
            try {
                for (LowLevelHeuristic lowLevelHeuristic : lowLevelHeuristics) {
                    rDebugWriter.write(lowLevelHeuristic.getR() + "\t");
                }
                rDebugWriter.write("\n");
            } catch (IOException ex) {
                Logger.getLogger(HyperHeuristic.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (nDebugWriter != null) {
            try {
                for (LowLevelHeuristic lowLevelHeuristic : lowLevelHeuristics) {
                    nDebugWriter.write(lowLevelHeuristic.getN() + "\t");
                }
                nDebugWriter.write("\n");
            } catch (IOException ex) {
                Logger.getLogger(HyperHeuristic.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if (lowLevelHeuristicsRankWriter != null) {
            try {
                for (LowLevelHeuristic lowLevelHeuristic : lowLevelHeuristics) {
                    lowLevelHeuristicsRankWriter.write(lowLevelHeuristic.getRank() + "\t");
                }
                lowLevelHeuristicsRankWriter.write("\n");
            } catch (IOException ex) {
                Logger.getLogger(HyperHeuristic.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (lowLevelHeuristicsTimeWriter != null) {
            try {
                //System.out.println("Escrevendo em : "+lowLevelHeuristicsTimeWriter.);
                for (LowLevelHeuristic lowLevelHeuristic : lowLevelHeuristics) {
                    lowLevelHeuristicsTimeWriter.write(lowLevelHeuristic.getElapsedTime() + "\t");
                }
                lowLevelHeuristicsTimeWriter.write("\n");
            } catch (IOException ex) {
                Logger.getLogger(HyperHeuristic.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
