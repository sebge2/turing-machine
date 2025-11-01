package be.sgerard.turing.operation.numeric;

import lombok.experimental.UtilityClass;

@UtilityClass
public class NumericOperationUtils {

    public static NumericFunction add(int left, int right) {
        return new NumericFunction(left, right, SumOperator.INSTANCE);
    }

    public static NumericFunction minus(int left, int right) {
        return new NumericFunction(left, right, MinusOperator.INSTANCE);
    }

    public static NumericFunction multiply(int left, int right) {
        return new NumericFunction(left, right, MultiplyOperator.INSTANCE);
    }

    public static NumericFunction divide(int left, int right) {
        return new NumericFunction(left, right, DivideOperator.INSTANCE);
    }
}
