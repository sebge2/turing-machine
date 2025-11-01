package be.sgerard.turing.operation;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.joining;

@UtilityClass
public class NumericOperationUtils {

    public static List<Integer> toNumericSymbols(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Invalid numeric value [" + value + "]");
        }

        return String.valueOf(value).chars()
                .mapToObj(c -> c - '0')
                .toList();
    }

    public static List<Integer> toNumericSymbols(int a, int b) {
        if (a < 0 || b < 0) {
            throw new IllegalArgumentException("Invalid numeric values [" + a + ", " + b + "]");
        }
        final List<Integer> result = new ArrayList<>(toNumericSymbols(a));
        result.add(null); // separator
        result.addAll(toNumericSymbols(b));
        return result;
    }

    public static int toIntValue(List<Integer> symbols) {
        if (symbols.isEmpty()) {
            return 0;
        }

        return Integer.parseInt(
                symbols.stream()
                        .filter(Objects::nonNull)
                        .map(Objects::toString)
                        .collect(joining())
        );
    }

    public static NumericOperation getNumericOperation(NumericOperationName name) {
        return switch (name) {
            case SUM -> new SimpleSumOperation();
            case SUBTRACT -> new SubtractOperation();
            case MULTIPLY -> new MultiplyOperation();
        };
    }
}
