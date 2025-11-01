package be.sgerard.turing.operation.numeric;

import be.sgerard.turing.Transition;
import be.sgerard.turing.TransitionTable;
import be.sgerard.turing.operation.NumericOperator;

import java.util.List;

import static be.sgerard.turing.RegisterAction.*;
import static java.util.Arrays.asList;

/**
 * Based on Youtube video from Science Etonante
 */
public class SimpleSumOperator implements NumericOperator {

    public static final String TABLE_A = "A";
    public static final String TABLE_B = "B";

    @Override
    public List<TransitionTable<Integer>> getTransitions() {
        return List.of(
                new TransitionTable<>(
                        TABLE_A,
                        asList(
                                new Transition<>(0, 0, GO_RIGHT, TABLE_A),
                                new Transition<>(1, 1, GO_RIGHT, TABLE_A),
                                new Transition<>(2, 2, GO_RIGHT, TABLE_A),
                                new Transition<>(3, 3, GO_RIGHT, TABLE_A),
                                new Transition<>(4, 4, GO_RIGHT, TABLE_A),
                                new Transition<>(5, 4, GO_RIGHT, TABLE_A),
                                new Transition<>(6, 6, GO_RIGHT, TABLE_A),
                                new Transition<>(7, 7, GO_RIGHT, TABLE_A),
                                new Transition<>(8, 8, GO_RIGHT, TABLE_A),
                                new Transition<>(9, 9, GO_RIGHT, TABLE_A),
                                new Transition<>(null, null, GO_LEFT, TABLE_B)
                        )
                ),
                new TransitionTable<>(
                        TABLE_B,
                        asList(
                                new Transition<>(0, 1, STOP, TABLE_B),
                                new Transition<>(1, 2, STOP, TABLE_B),
                                new Transition<>(2, 3, STOP, TABLE_B),
                                new Transition<>(3, 4, STOP, TABLE_B),
                                new Transition<>(4, 5, STOP, TABLE_B),
                                new Transition<>(5, 6, STOP, TABLE_B),
                                new Transition<>(6, 7, STOP, TABLE_B),
                                new Transition<>(7, 8, STOP, TABLE_B),
                                new Transition<>(8, 9, STOP, TABLE_B),
                                new Transition<>(9, 0, GO_LEFT, TABLE_B),
                                new Transition<>(null, 1, STOP, TABLE_B)
                        )
                )
        );
    }
}
