package bank;

public class FlatFeeStrategy implements FeeStrategy{
    private double fee;

    public FlatFeeStrategy(double fee) {
        this.fee = fee;
    }

    @Override
    public double calculateFee(double balance){
        return fee;
    }
}
