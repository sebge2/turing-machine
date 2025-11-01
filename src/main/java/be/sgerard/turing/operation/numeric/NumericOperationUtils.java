package be.sgerard.turing.operation.numeric;

import lombok.experimental.UtilityClass;

@UtilityClass
public class NumericOperationUtils {

    public static NumericFunction add(int left, int right) {
        return new NumericFunction(left, right, new SumOperator());
    }

    public static NumericFunction minus(int left, int right) {
        return new NumericFunction(left, right, new MinusOperator());
    }

    public static NumericFunction multiply(int left, int right) {
        return new NumericFunction(left, right, new MultiplyOperator());
    }
}
