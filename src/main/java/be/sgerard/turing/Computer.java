package be.sgerard.turing;

import be.sgerard.turing.operation.Function;
import be.sgerard.turing.operation.Operator;

import java.util.List;

public class Computer {

    public <I, O, S> O compute(Function<I, O, S> function) {
        final Operator<S> operation = function.getOperator();

        final List<TransitionTable<S>> transitions = operation.getTransitions();
        final GlobalState<S> state = new GlobalState<>(transitions, transitions.getFirst(), Register.create(function.toState(function.getOperands())));

        return function.fromState(
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
