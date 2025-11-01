package be.sgerard.turing.operation;

import be.sgerard.turing.RegisterAction;
import be.sgerard.turing.Transition;
import be.sgerard.turing.TransitionTable;

import java.util.ArrayList;
import java.util.List;

import static be.sgerard.turing.RegisterAction.*;
import static java.util.Arrays.asList;

/**
 * Multiply two single-digit operands encoded on the tape as [A][null][B].
 * Result (one or two digits) is written on the tape (old separator and B are cleared).
 *
 * Stage 1.1 limitation: A and B must be single digits 0..9.
 *
 * @author Jetbrains Junie
 */
public class MultiplyOperation implements NumericOperation {

    // Generic state names
    private static final String READ_A = "READ_A";

    // For each a in 0..9 we create family of states with prefix F{a}_*
    private static String F(int a, String suffix) {
        return "F" + a + "_" + suffix;
    }

    @Override
    public NumericOperationName getName() {
        return NumericOperationName.MULTIPLY;
    }

    @Override
    public List<TransitionTable<Integer>> getTransitions() {
        final List<TransitionTable<Integer>> tables = new ArrayList<>();

        // READ_A: capture A and initialize accumulator to 0 at position 0, then move to separator for that family
        final List<Transition<Integer>> readATransitions = new ArrayList<>();
        for (int a = 0; a <= 9; a++) {
            // overwrite A with 0 (accumulator ones place), go right and enter the family state that seeks separator
            readATransitions.add(new Transition<>(a, 0, GO_RIGHT, F(a, "SEEK_SEP")));
        }
        tables.add(new TransitionTable<>(READ_A, readATransitions));

        for (int a = 0; a <= 9; a++) {
            // SEEK_SEP: move right until null, then move right to B and enter LOOP
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
                            new Transition<>(null, null, GO_RIGHT, F(a, "LOOP"))
                    )
            ));

            // LOOP at B position: if B==0 -> CLEANUP; else decrement B and go left-left to ADD0
            final List<Transition<Integer>> loopTransitions = new ArrayList<>();
            loopTransitions.add(new Transition<>(0, null, GO_LEFT, F(a, "CLEAN1"))); // write null over B and cleanup
            for (int b = 1; b <= 9; b++) {
                loopTransitions.add(new Transition<>(b, b - 1, GO_LEFT, F(a, "LEFT1")));
            }
            tables.add(new TransitionTable<>(F(a, "LOOP"), loopTransitions));

            // LEFT1: currently at separator (null), move left to accumulator ones (position 0)
            tables.add(new TransitionTable<>(
                    F(a, "LEFT1"),
                    asList(
                            new Transition<>(null, null, GO_LEFT, F(a, "ADD0")),
                            // safety on digits
                            new Transition<>(0, 0, GO_LEFT, F(a, "ADD0")),
                            new Transition<>(1, 1, GO_LEFT, F(a, "ADD0")),
                            new Transition<>(2, 2, GO_LEFT, F(a, "ADD0")),
                            new Transition<>(3, 3, GO_LEFT, F(a, "ADD0")),
                            new Transition<>(4, 4, GO_LEFT, F(a, "ADD0")),
                            new Transition<>(5, 5, GO_LEFT, F(a, "ADD0")),
                            new Transition<>(6, 6, GO_LEFT, F(a, "ADD0")),
                            new Transition<>(7, 7, GO_LEFT, F(a, "ADD0")),
                            new Transition<>(8, 8, GO_LEFT, F(a, "ADD0")),
                            new Transition<>(9, 9, GO_LEFT, F(a, "ADD0"))
                    )
            ));

            // ADD0: add constant 'a' to the ones place, branch on carry
            final List<Transition<Integer>> add0Transitions = new ArrayList<>();
            for (int x = 0; x <= 9; x++) {
                int sum = x + a;
                if (sum < 10) {
                    add0Transitions.add(new Transition<>(x, sum, RegisterAction.GO_RIGHT, F(a, "TO_B")));
                } else {
                    add0Transitions.add(new Transition<>(x, sum - 10, RegisterAction.GO_LEFT, F(a, "CARRY1")));
                }
            }
            // safety if null encountered (shouldn't happen): treat as 0
            if (a < 10) {
                int sum = a; // 0 + a
                if (sum < 10) {
                    add0Transitions.add(new Transition<>(null, sum, RegisterAction.GO_RIGHT, F(a, "TO_B")));
                } else {
                    add0Transitions.add(new Transition<>(null, sum - 10, RegisterAction.GO_LEFT, F(a, "CARRY1")));
                }
            }
            tables.add(new TransitionTable<>(F(a, "ADD0"), add0Transitions));

            // CARRY1: propagate carry of 1 to the left, then go to TO_B
            tables.add(new TransitionTable<>(
                    F(a, "CARRY1"),
                    asList(
                            new Transition<>(null, 1, GO_RIGHT, F(a, "TO_B")),
                            new Transition<>(0, 1, GO_RIGHT, F(a, "TO_B")),
                            new Transition<>(1, 2, GO_RIGHT, F(a, "TO_B")),
                            new Transition<>(2, 3, GO_RIGHT, F(a, "TO_B")),
                            new Transition<>(3, 4, GO_RIGHT, F(a, "TO_B")),
                            new Transition<>(4, 5, GO_RIGHT, F(a, "TO_B")),
                            new Transition<>(5, 6, GO_RIGHT, F(a, "TO_B")),
                            new Transition<>(6, 7, GO_RIGHT, F(a, "TO_B")),
                            new Transition<>(7, 8, GO_RIGHT, F(a, "TO_B")),
                            new Transition<>(8, 9, GO_RIGHT, F(a, "TO_B")),
                            new Transition<>(9, 0, GO_LEFT, F(a, "CARRY1"))
                    )
            ));

            // TO_B: from anywhere left of separator, move right until separator, then right to B and continue LOOP
            tables.add(new TransitionTable<>(
                    F(a, "TO_B"),
                    asList(
                            new Transition<>(0, 0, GO_RIGHT, F(a, "TO_B")),
                            new Transition<>(1, 1, GO_RIGHT, F(a, "TO_B")),
                            new Transition<>(2, 2, GO_RIGHT, F(a, "TO_B")),
                            new Transition<>(3, 3, GO_RIGHT, F(a, "TO_B")),
                            new Transition<>(4, 4, GO_RIGHT, F(a, "TO_B")),
                            new Transition<>(5, 5, GO_RIGHT, F(a, "TO_B")),
                            new Transition<>(6, 6, GO_RIGHT, F(a, "TO_B")),
                            new Transition<>(7, 7, GO_RIGHT, F(a, "TO_B")),
                            new Transition<>(8, 8, GO_RIGHT, F(a, "TO_B")),
                            new Transition<>(9, 9, GO_RIGHT, F(a, "TO_B")),
                            new Transition<>(null, null, GO_RIGHT, F(a, "LOOP"))
                    )
            ));

            // CLEAN1: currently at separator after nulling B; ensure separator is null and stop
            tables.add(new TransitionTable<>(
                    F(a, "CLEAN1"),
                    asList(
                            new Transition<>(null, null, STOP, F(a, "CLEAN1")),
                            // safety fallbacks
                            new Transition<>(0, 0, STOP, F(a, "CLEAN1")),
                            new Transition<>(1, 1, STOP, F(a, "CLEAN1")),
                            new Transition<>(2, 2, STOP, F(a, "CLEAN1")),
                            new Transition<>(3, 3, STOP, F(a, "CLEAN1")),
                            new Transition<>(4, 4, STOP, F(a, "CLEAN1")),
                            new Transition<>(5, 5, STOP, F(a, "CLEAN1")),
                            new Transition<>(6, 6, STOP, F(a, "CLEAN1")),
                            new Transition<>(7, 7, STOP, F(a, "CLEAN1")),
                            new Transition<>(8, 8, STOP, F(a, "CLEAN1")),
                            new Transition<>(9, 9, STOP, F(a, "CLEAN1"))
                    )
            ));
        }

        return tables;
    }
}
