package be.sgerard.turing.operation;

import java.util.List;

public interface Function<I, O, S> {

    List<I> getOperands();

    List<S> toState(List<I> values);

    O fromState(List<S> state);

    Operator<S> getOperator();

}
