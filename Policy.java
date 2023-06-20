import java.util.*;
import java.text.*;

public class Policy {
    private double[] actionProb;
    private double fitness;
    private int size, pos;
    private Random rand;
    DecimalFormat decFormat = new DecimalFormat("0.000");

    public Policy(double[] prob, Random r) {
        size = prob.length;
        rand = r;
        fitness = 0.0;
        actionProb = new double[size];

        // generate probabilites that add up to 1.0
        for (int i = 0; i < size; i++)
            actionProb[i] = prob[i];
    }

    public Policy(int len, Random r) {
        size = len;
        rand = r;
        double rem = 1.0;
        fitness = 0.0;
        actionProb = new double[size];

        // generate probabilites that add up to 1.0
        for (int i = 0; i < size - 1; i++) {
            actionProb[i] = rand.nextDouble() * rem;
            rem -= actionProb[i];
        }
        actionProb[size - 1] = rem;
    }

    public Policy(int len, int p, Random r) {
        size = len;
        fitness = 0.0;
        pos = p;
        rand = r;
        actionProb = new double[size];

        // generate probabilites that add up to 1.0
        for (int i = 0; i < size; i++) {
            actionProb[i] = 0.0;
        }
        actionProb[pos] = 1.0;
    }

    public void setFitness(double f) {
        fitness = f;
    }

    public double getFitness() {
        return fitness;
    }

    public double getGene(int i) {
        return actionProb[i];
    }

    public void setGene(int i, double val) {
        actionProb[i] = val;
    }

    public int getAction() {
        double num = rand.nextDouble();

        double cum = 0.0;

        for (int i = 0; i < size; i++) {
            cum += actionProb[i];
            if (cum >= num) {
                return i;
            }
        }

        return 0;
    }

    protected Object clone() {
        Policy pol = new Policy(size, rand);

        for (int i = 0; i < size; i++)
            pol.setGene(i, actionProb[i]);

        pol.setFitness(fitness);

        return pol;
    } // clone

    public String toString() {
        String pol = "";

        for (int i = 0; i < size; i++)
            pol += decFormat.format(actionProb[i]) + "\t";

        return pol;
    } // toString

}