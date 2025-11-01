package be.sgerard.turing.operation.numeric;

import be.sgerard.turing.RegisterAction;
import be.sgerard.turing.Transition;
import be.sgerard.turing.TransitionTable;
import be.sgerard.turing.operation.NumericOperator;

import java.util.ArrayList;
import java.util.List;

import static be.sgerard.turing.RegisterAction.*;
import static java.util.Arrays.asList;

/**
 * Add two single-digit operands encoded on the tape as [A][null][B].
 * Result (one or two digits) is written on the tape (separator and B are cleared).
 *
 * Stage 1.1 limitation: A and B must be single digits 0..9.
 *
 * This mirrors the general approach used by {@link MultiplyOperator} by generating
 * family states per first operand (and second when needed) and handling carry propagation.
 *
 * @author Jetbrains Junie
 */
public class SumOperator implements NumericOperator {

    public static final SumOperator INSTANCE = new SumOperator();

    private static final String READ_A = "READ_A";

    private static String F(int a, String suffix) {
        return "F" + a + "_" + suffix;
    }

    private static String FB(int a, int b, String suffix) {
        return "F" + a + "_B" + b + "_" + suffix;
    }

    @Override
    public List<TransitionTable<Integer>> getTransitions() {
        final List<TransitionTable<Integer>> tables = new ArrayList<>();

        // READ_A: capture A and proceed to seek the separator for that family
        final List<Transition<Integer>> readATransitions = new ArrayList<>();
        for (int a = 0; a <= 9; a++) {
            // keep A as-is, move right to find separator
            readATransitions.add(new Transition<>(a, a, GO_RIGHT, F(a, "SEEK_SEP")));
        }
        tables.add(new TransitionTable<>(READ_A, readATransitions));

        for (int a = 0; a <= 9; a++) {
            // SEEK_SEP: move right until null, then move right to B and branch by B
            tables.add(new TransitionTable<>(
                    F(a, "SEEK_SEP"),
                    asList(
                            new Transition<>(0, 0, GO_RIGHT, F(a, "SEEK_SEP")),
                            new Transition<>(1, 1, GO_RIGHT, F(a, "SEEK_SEP")),
                            new Transition<>(2, 2, GO_RIGHT, F(a, "SEEK_SEP")),
                            new Transition<>(3, 3, GO_RIGHT, F(a, "SEEK_SEP")),
                            new Transition<>(4, 4, GO_RIGHT, F(a, "SEEK_SEP")),
                            new Transition<>(5, 5, GO_RIGHT, F(a, "SEEK_SEP")),
                            new Transition<>(6, 6, GO_RIGHT, F(a, "SEEK_SEP")),
                            new Transition<>(7, 7, GO_RIGHT, F(a, "SEEK_SEP")),
                            new Transition<>(8, 8, GO_RIGHT, F(a, "SEEK_SEP")),
                            new Transition<>(9, 9, GO_RIGHT, F(a, "SEEK_SEP")),
                            new Transition<>(null, null, GO_RIGHT, F(a, "READ_B"))
                    )
            ));

            // READ_B: at B position, branch based on B; overwrite B with null and go left over separator
            final List<Transition<Integer>> readBTransitions = new ArrayList<>();
            for (int b = 0; b <= 9; b++) {
                readBTransitions.add(new Transition<>(b, null, GO_LEFT, FB(a, b, "LEFT1")));
            }
            // Safety: if already null (no B), treat as 0 - nothing to add, just stop
            readBTransitions.add(new Transition<>(null, null, STOP, F(a, "READ_B")));
            tables.add(new TransitionTable<>(F(a, "READ_B"), readBTransitions));

            for (int b = 0; b <= 9; b++) {
                // LEFT1: from B (now null) move left to cross separator (null) to ones place (A)
                tables.add(new TransitionTable<>(
                        FB(a, b, "LEFT1"),
                        asList(
                                new Transition<>(null, null, GO_LEFT, FB(a, b, "ADD0")),
                                // safety on digits
                                new Transition<>(0, 0, GO_LEFT, FB(a, b, "ADD0")),
                                new Transition<>(1, 1, GO_LEFT, FB(a, b, "ADD0")),
                                new Transition<>(2, 2, GO_LEFT, FB(a, b, "ADD0")),
                                new Transition<>(3, 3, GO_LEFT, FB(a, b, "ADD0")),
                                new Transition<>(4, 4, GO_LEFT, FB(a, b, "ADD0")),
                                new Transition<>(5, 5, GO_LEFT, FB(a, b, "ADD0")),
                                new Transition<>(6, 6, GO_LEFT, FB(a, b, "ADD0")),
                                new Transition<>(7, 7, GO_LEFT, FB(a, b, "ADD0")),
                                new Transition<>(8, 8, GO_LEFT, FB(a, b, "ADD0")),
                                new Transition<>(9, 9, GO_LEFT, FB(a, b, "ADD0"))
                        )
                ));

                // ADD0: add constant 'b' to current digit (initially 'a'), branch on carry
                final List<Transition<Integer>> add0Transitions = new ArrayList<>();
                for (int x = 0; x <= 9; x++) {
                    int sum = x + b;
                    if (sum < 10) {
                        add0Transitions.add(new Transition<>(x, sum, RegisterAction.STOP, FB(a, b, "ADD0")));
                    } else {
                        add0Transitions.add(new Transition<>(x, sum - 10, RegisterAction.GO_LEFT, FB(a, b, "CARRY1")));
                    }
                }
                // safety: if null encountered (shouldn't happen), treat as 0
                int sum = b; // 0 + b
                if (sum < 10) {
                    add0Transitions.add(new Transition<>(null, sum, RegisterAction.STOP, FB(a, b, "ADD0")));
                } else {
                    add0Transitions.add(new Transition<>(null, sum - 10, RegisterAction.GO_LEFT, FB(a, b, "CARRY1")));
                }
                tables.add(new TransitionTable<>(FB(a, b, "ADD0"), add0Transitions));

                // CARRY1: propagate carry of 1 to the left, then stop
                tables.add(new TransitionTable<>(
                        FB(a, b, "CARRY1"),
                        asList(
                                new Transition<>(null, 1, STOP, FB(a, b, "CARRY1")),
                                new Transition<>(0, 1, STOP, FB(a, b, "CARRY1")),
                                new Transition<>(1, 2, STOP, FB(a, b, "CARRY1")),
                                new Transition<>(2, 3, STOP, FB(a, b, "CARRY1")),
                                new Transition<>(3, 4, STOP, FB(a, b, "CARRY1")),
                                new Transition<>(4, 5, STOP, FB(a, b, "CARRY1")),
                                new Transition<>(5, 6, STOP, FB(a, b, "CARRY1")),
                                new Transition<>(6, 7, STOP, FB(a, b, "CARRY1")),
                                new Transition<>(7, 8, STOP, FB(a, b, "CARRY1")),
                                new Transition<>(8, 9, STOP, FB(a, b, "CARRY1")),
                                new Transition<>(9, 0, GO_LEFT, FB(a, b, "CARRY1"))
                        )
                ));
            }
        }

        return tables;
    }
}
