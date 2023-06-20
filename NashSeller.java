import java.util.*;

public class NashSeller {
    private double bidPrice, bandSize, profit, marginalCost;
    private double priceBandLL[], priceBandUL[], probDist[];
    private int qty, priceBand, noOfBands;
    private Random rand;
    // private double refReward;

    public NashSeller(int bands, double reservePrice, double cost, Random r) {
        rand = r;
        noOfBands = bands;
        marginalCost = cost;
        bandSize = (reservePrice - marginalCost) / bands;
        priceBandLL = new double[noOfBands];
        priceBandUL = new double[noOfBands];
        probDist = new double[noOfBands];

        initialize(marginalCost, bandSize);
        // refReward = 2000.0;

    }

    public void initialize(double min, double size) {
        bandSize = size;

        // initialize the arrays
        for (int i = 0; i < noOfBands; i++) {
            priceBandLL[i] = min + i * bandSize;
            priceBandUL[i] = priceBandLL[i] + bandSize;
            probDist[i] = 0.0;
        }
        probDist[5] = 0.22;
        probDist[6] = 0.28;
        probDist[7] = 0.21;
        probDist[8] = 0.16;
        probDist[9] = 0.13;

        updatePriceBand();
        updateBidPrice();
    }

    public void setQty(int q) {
        qty = q;
        profit = (bidPrice - marginalCost) * qty;
        updatePriceBand();
        updateBidPrice();
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
                rand.nextDouble() * bandSize;
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

}
