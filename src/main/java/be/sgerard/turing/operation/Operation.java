package be.sgerard.turing.operation;


import be.sgerard.turing.TransitionTable;

import java.util.List;

public interface Operation<S > {

    NumericOperationName getName();

    List<TransitionTable<S>> getTransitions();

}
