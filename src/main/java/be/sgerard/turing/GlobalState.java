package be.sgerard.turing;

import java.util.List;

public record GlobalState<S>(List<TransitionTable<S>> transitionTables,
                             TransitionTable<S> currentTable,
                             Register<S> register) {
    public GlobalState<S> update(Register<S> register, String currentTable) {


        return new GlobalState<>(transitionTables, transitionTables.stream().filter(table -> table.name().equals(currentTable)).findFirst().orElseThrow(), register);
    }
}
