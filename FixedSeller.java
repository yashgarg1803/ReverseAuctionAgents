import java.util.*;

public class FixedSeller {
    private double bidPrice, bandSize, profit, marginalCost;
    private double priceBandLL[], priceBandUL[];
    private int qty, priceBand, noOfBands;
    private Random rand;

    public FixedSeller(int bands, double reservePrice, double cost, Random r,
            int bid) {
        rand = r;
        noOfBands = bands;
        marginalCost = cost;
        priceBand = bid;
        bandSize = (reservePrice - marginalCost) / bands;
        priceBandLL = new double[noOfBands];
        priceBandUL = new double[noOfBands];

        initialize(marginalCost, bandSize);

    }

    public void initialize(double min, double size) {
        bandSize = size;

        // initialize the arrays
        for (int i = 0; i < noOfBands; i++) {
            priceBandLL[i] = min + i * bandSize;
            priceBandUL[i] = priceBandLL[i] + bandSize;
        }

        updateBidPrice();
    }

    public void setQty(int q) {
        qty = q;
        profit = (bidPrice - marginalCost) * qty;
        updateBidPrice();
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

}
