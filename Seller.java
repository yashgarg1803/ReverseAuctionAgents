import java.util.*;

public class Seller {
    public static final int EPS_GREEDY = 2, SOFTMAX = 1;
    public static final int SAMPLE_AVG = 0, TIME_WT = 1;
    private double bidPrice, bandSize, profit, marginalCost, estProfit[];
    private double priceBandLL[], priceBandUL[], probDist[];
    private int qty, timeUnit[], priceBand, noOfBands;
    private int actionVal, updateRule;
    private Random rand;

    public Seller(int bands, double reservePrice, double cost, Random r,
            int action, int update) {
        rand = r;
        noOfBands = bands;
        marginalCost = cost;
        actionVal = action;
        updateRule = update;
        bandSize = (reservePrice - marginalCost) / bands;
        priceBandLL = new double[noOfBands];
        priceBandUL = new double[noOfBands];
        estProfit = new double[noOfBands];
        probDist = new double[noOfBands];
        timeUnit = new int[noOfBands];

        initialize(marginalCost, bandSize);

    }

    public void initialize(double min, double size) {
        bandSize = size;

        // initialize the arrays
        for (int i = 0; i < noOfBands; i++) {
            priceBandLL[i] = min + i * bandSize;
            priceBandUL[i] = priceBandLL[i] + bandSize;
            timeUnit[i] = 0;
            probDist[i] = 1.0 / noOfBands;

            // initializing the estimates Q0
            estProfit[i] = 2000.0;
        }

        updatePriceBand();
        updateBidPrice();
    }

    private void updateEstProfit() {
        switch (updateRule) {
            case (SAMPLE_AVG):
                estProfit[priceBand] += (profit - estProfit[priceBand]) / timeUnit[priceBand];
                break;

            case (TIME_WT):
                estProfit[priceBand] += (profit - estProfit[priceBand]) / 10;
                break;

        }
    }

    public void setQty(int q) {
        qty = q;
        timeUnit[priceBand]++;
        profit = (bidPrice - marginalCost) * qty;
        updateEstProfit();

        // update prob. distribution
        switch (actionVal) {
            case (EPS_GREEDY):
                greedyDist(0.1);
                break;
            case (SOFTMAX):
                softMaxDist();
                break;
            case (SAMPLE_AVG):
                smplAvgDist();
                break;
        }

        updatePriceBand();
        updateBidPrice();
    }

    // Epsilon-greedy action value method
    // where 0 <= epsilon <= 1
    private void greedyDist(double epsilon) {
        double max = getMax(estProfit);
        int maxFreq = getFreq(estProfit, max);

        for (int i = 0; i < noOfBands; i++) {
            if (estProfit[i] == max)
                probDist[i] = (1 - epsilon) / maxFreq;
            else
                probDist[i] = epsilon / (noOfBands - maxFreq);
        }
    }

    private double getMax(double array[]) {
        double max = array[0];

        for (int i = 1; i < array.length; i++)
            if (array[i] > max)
                max = array[i];

        return max;
    }

    private int getFreq(double array[], double num) {
        int count = 0;

        for (int i = 0; i < array.length; i++)
            if (array[i] == num)
                count++;

        return count;
    }

    private double getSum(double array[]) {
        double sum = 0.0;

        for (int i = 0; i < array.length; i++)
            sum += array[i];

        return sum;
    }

    // Soft-max action value method
    // i.e., prob. dist. is graded function of estProfit
    // uses Gibbs distribution with parameter tau
    private void softMaxDist() {
        double tau = 50.0;
        double exp[] = new double[noOfBands];

        for (int i = 0; i < noOfBands; i++)
            exp[i] = Math.exp(estProfit[i] / tau);

        double sum = getSum(exp);

        for (int i = 0; i < noOfBands; i++)
            probDist[i] = exp[i] / sum;
    }

    // Sample average action value method
    // i.e., prob. dist. is sample average of estProfit
    private void smplAvgDist() {

        double sum = getSum(estProfit);

        for (int i = 0; i < noOfBands; i++)
            probDist[i] = estProfit[i] / sum;
    }

    private void updatePriceBand() {
        double cum = 0.0;
        double num = rand.nextDouble();

        for (int i = 0; i < noOfBands; i++) {
            cum += probDist[i];
            if (cum >= num) {
                priceBand = i;
                return;
            }
        }
    }

    private void updateBidPrice() {
        bidPrice = priceBandLL[priceBand] +
                rand.nextDouble() * (priceBandUL[priceBand] - priceBandLL[priceBand]);
    }

    public double getBidPrice() {
        return bidPrice;
    }

    public double getProfit() {
        return profit;
    }

    public double[] getProbDist() {
        return probDist;
    }

    public double[] getEstProfit() {
        return estProfit;
    }

    public double[] getPriceBandLL() {
        return priceBandLL;
    }

    public double[] getPriceBandUL() {
        return priceBandUL;
    }

}
