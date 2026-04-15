package bank.strategy;

public class PercentageFeeStrategy implements  FeeStrategy {
    private double percentage;

    public PercentageFeeStrategy(double percentage) {
        this.percentage = percentage;
    }

    @Override
    public  double calculateFee(double balance){
        return  balance * percentage;
    }
}
