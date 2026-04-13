package bank;

public class NoFeeStrategy implements FeeStrategy{

    @Override
    public double calculateFee(double balance){
        return 0;
    }
}
