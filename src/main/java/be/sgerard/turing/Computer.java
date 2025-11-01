package be.sgerard.turing;

import be.sgerard.turing.operation.NumericOperation;
import be.sgerard.turing.operation.NumericOperationName;

import java.util.List;

import static be.sgerard.turing.operation.NumericOperationUtils.*;

public class Computer {

    public int compute(int input, NumericOperationName operationName) {
        final NumericOperation operation = getNumericOperation(operationName);

        final List<TransitionTable<Integer>> transitions = operation.getTransitions();
        final GlobalState<Integer> state = new GlobalState<>(transitions, transitions.getFirst(), Register.create(toNumericSymbols(input)));

        return toIntValue(
                doCompute(state)
        );
    }

    public int compute(int a, int b, NumericOperationName operationName) {
        final NumericOperation operation = getNumericOperation(operationName);

        final List<TransitionTable<Integer>> transitions = operation.getTransitions();
        final GlobalState<Integer> state = new GlobalState<>(transitions, transitions.getFirst(), Register.create(toNumericSymbols(a, b)));

        return toIntValue(
                doCompute(state)
        );
    }

    private <S> List<S> doCompute(GlobalState<S> state) {
        final S originalSymbol = state.register().current().orElse(null);
        final Transition<S> transition = state.currentTable().getTransition(originalSymbol);

        final Register<S> updatedRegister = state
                .register()
                .update(transition.target())
                .perform(transition.registerAction());

        return switch (transition.registerAction()) {
            case STOP -> updatedRegister.symbols();
            case GO_LEFT, GO_RIGHT -> doCompute(
                    state.update(
                            updatedRegister,
                            transition.nextTable()
                    )
            );
        };
    }
}
