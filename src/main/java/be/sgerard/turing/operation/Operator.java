package be.sgerard.turing.operation;


import be.sgerard.turing.TransitionTable;

import java.util.List;

public interface Operator<S > {

    List<TransitionTable<S>> getTransitions();

}
