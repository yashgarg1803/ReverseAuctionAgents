import java.io.*;
import java.text.*;
import java.util.*;

public class MarketPlace {
    private static int noOfActions = 10;

    private static double distance(Policy pol1, Policy pol2) {
        double dist = 0.0;

        for (int i = 0; i < noOfActions; i++)
            dist += (pol1.getGene(i) - pol2.getGene(i)) * (pol1.getGene(i) - pol2.getGene(i));

        dist = Math.sqrt(dist);

        return dist;
    } // distance

    public static void main(String[] arg) throws Exception {
        int demand, indCapacity, seller1Qty, seller2Qty, cycles;
        double seller1Cost, seller2Cost, reservePrice, dist[], bandLL[], bandUL[];
        double profit1, profit2;
        Seller seller1;
        Seller seller2;
        // NashSeller seller2;
        Policy nashPolicy;
        PrintWriter outProfit1, outProfit2, outDist1, outDist2;
        PrintWriter outBid1, outBid2;
        DecimalFormat decFormat = new DecimalFormat("0.00");
        Random rand = new Random(90);

        // initialize
        demand = 100;
        indCapacity = 65;
        cycles = 3000;
        seller1Cost = seller2Cost = 40.0;
        reservePrice = 80.0;

        nashPolicy = new Policy(noOfActions, 9, rand);

        for (int i = 0; i < noOfActions; i++)
            nashPolicy.setGene(i, 0.0);

        nashPolicy.setGene(5, 0.22);
        nashPolicy.setGene(6, 0.28);
        nashPolicy.setGene(7, 0.21);
        nashPolicy.setGene(8, 0.16);
        nashPolicy.setGene(9, 0.13);

        // open the output files
        outProfit1 = new PrintWriter(new FileWriter("profit1.txt"), true);
        outProfit2 = new PrintWriter(new FileWriter("profit2.txt"), true);
        outDist1 = new PrintWriter(new FileWriter("dist1.txt"), true);
        outDist2 = new PrintWriter(new FileWriter("dist2.txt"), true);
        outBid1 = new PrintWriter(new FileWriter("bid1.txt"), true);
        outBid2 = new PrintWriter(new FileWriter("bid2.txt"), true);

        // create the sellers
        seller1 = new Seller(10, reservePrice, seller1Cost, rand,
                Seller.SOFTMAX, Seller.TIME_WT);
        seller2 = new Seller(10, reservePrice, seller2Cost, rand,
                Seller.SAMPLE_AVG, Seller.TIME_WT);
        // seller2 = new NashSeller( 10, reservePrice, seller2Cost, rand );
        // seller2 = new FixedSeller( 10, reservePrice, seller2Cost, rand, 9 );

        // go through the simulation cycles
        for (int i = 1; i < cycles; i++) {
            profit1 = profit2 = 0;

            for (int j = 0; j < 10; j++) {

                if (seller1.getBidPrice() < seller2.getBidPrice()) {
                    // seller1 sells to capacity & seller2 gets the
                    // remainder
                    seller1.setQty(indCapacity);
                    seller2.setQty(demand - indCapacity);
                } else {
                    if (seller2.getBidPrice() < seller1.getBidPrice()) {
                        // seller2 sells to capacity & seller1 gets the
                        // remainder
                        seller2.setQty(indCapacity);
                        seller1.setQty(demand - indCapacity);
                    } else {
                        // both bid the same price, so they get
                        // same amount
                        seller1.setQty(demand / 2);
                        seller2.setQty(demand / 2);
                    }
                }

                profit1 += seller1.getProfit();
                profit2 += seller2.getProfit();
            }

            // write the output
            outProfit1.println(decFormat.format(profit1 / 10));
            outProfit2.println(decFormat.format(profit2 / 10));

            outBid1.println(decFormat.format(seller1.getBidPrice()));
            outBid2.println(decFormat.format(seller2.getBidPrice()));

            outDist1.println(distance(nashPolicy, new Policy(seller1.getProbDist(), rand)));
            outDist2.println(distance(nashPolicy, new Policy(seller2.getProbDist(), rand)));

        } // for

        dist = seller1.getProbDist();
        bandUL = seller1.getPriceBandUL();
        bandLL = seller1.getPriceBandLL();
        for (int j = 0; j < dist.length; j++)
            outDist1.print(decFormat.format(bandLL[j]) + "-" + decFormat.format(bandUL[j]) + "\t");
        outDist1.println();
        for (int j = 0; j < dist.length; j++)
            outDist1.print(decFormat.format(dist[j]) + "\t");
        outDist1.println();

        dist = seller2.getProbDist();
        bandUL = seller2.getPriceBandUL();
        bandLL = seller2.getPriceBandLL();
        for (int j = 0; j < dist.length; j++)
            outDist2.print(decFormat.format(bandLL[j]) + "-" + decFormat.format(bandUL[j]) + "\t");
        outDist2.println();
        for (int j = 0; j < dist.length; j++)
            outDist2.print(decFormat.format(dist[j]) + " \t");
        outDist2.println();

        outProfit1.close();
        outProfit2.close();
        outBid1.close();
        outBid2.close();
        outDist1.close();
        outDist2.close();

    } // main
} // class MarketPlace
