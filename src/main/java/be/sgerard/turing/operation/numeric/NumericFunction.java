package be.sgerard.turing.operation.numeric;

import be.sgerard.turing.operation.Function;
import be.sgerard.turing.operation.NumericOperator;
import be.sgerard.turing.operation.Operator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

public record NumericFunction(int left, int right,
                              NumericOperator operator) implements Function<Integer, Integer, Integer> {

    @Override
    public List<Integer> getOperands() {
        return asList(left, right);
    }

    @Override
    public List<Integer> toState(List<Integer> values) {
        final List<Integer> result = new ArrayList<>(toNumericSymbols(left));
        result.add(null); // separator
        result.addAll(toNumericSymbols(right));
        return result;
    }

    @Override
    public Integer fromState(List<Integer> state) {
        if (state.isEmpty()) {
            return 0;
        }

        return Integer.parseInt(
                state.stream()
                        .filter(Objects::nonNull)
                        .map(Objects::toString)
                        .collect(joining())
        );
    }

    @Override
    public Operator<Integer> getOperator() {
        return operator();
    }

    private List<Integer> toNumericSymbols(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Invalid numeric value [" + value + "]");
        }

        return String.valueOf(value).chars()
                .mapToObj(c -> c - '0')
                .toList();
    }
}
