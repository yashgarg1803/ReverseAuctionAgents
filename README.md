# Bidding Agents for Reverse Auctions

Tested 3 bidding agents with different probability distributions for picking a bidding price in a reverse auction.

The pdfs tested were all reinforcement learning functions: sample average, greedy, and softmax.

Found softmax to be able to beat results from Nash Equilibrium strategy when played against a suite of opponents.

Ensured that softmax was a significantly different strategy from the Nash Equilibrium.

Full detailed results in [the results.xlsx spreadsheet](./results.xlsx).