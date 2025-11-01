package be.sgerard.turing.operation.numeric;

import be.sgerard.turing.Transition;
import be.sgerard.turing.TransitionTable;
import be.sgerard.turing.operation.NumericOperator;

import java.util.ArrayList;
import java.util.List;

import static be.sgerard.turing.RegisterAction.*;
import static java.util.Arrays.asList;

/**
 * Divide two single-digit operands encoded on the tape as [A][null][B].
 * Result (single digit 0..9) is written over A; the separator and B are cleared.
 *
 * Stage 1.1 limitation: A and B must be single digits 0..9. Division is integer floor division.
 * For B == 0, the result is defined as 0 (graceful handling to avoid undefined behavior).
 *
 * Mirrors the style of SumOperator/MinusOperator simple binary path.
 */
public class DivideOperator implements NumericOperator {

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

        // READ_A: capture A and seek separator for that family
        final List<Transition<Integer>> readATransitions = new ArrayList<>();
        for (int a = 0; a <= 9; a++) {
            readATransitions.add(new Transition<>(a, a, GO_RIGHT, F(a, "SEEK_SEP")));
        }
        tables.add(new TransitionTable<>(READ_A, readATransitions));

        for (int a = 0; a <= 9; a++) {
            // SEEK_SEP: move right until null, then right to B and branch on B
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

            // READ_B: at B position, branch on B (0..9). Overwrite B with null, move left to separator
            final List<Transition<Integer>> readBTransitions = new ArrayList<>();
            for (int b = 0; b <= 9; b++) {
                readBTransitions.add(new Transition<>(b, null, GO_LEFT, FB(a, b, "LEFT1")));
            }
            // Safety: if already null (no B), treat as division by 0 and stop after cleanup
            readBTransitions.add(new Transition<>(null, null, GO_LEFT, FB(a, 0, "LEFT1")));
            tables.add(new TransitionTable<>(F(a, "READ_B"), readBTransitions));

            // LEFT1: cross separator to A position
            for (int b = 0; b <= 9; b++) {
                tables.add(new TransitionTable<>(
                        FB(a, b, "LEFT1"),
                        asList(
                                new Transition<>(null, null, GO_LEFT, FB(a, b, "WRITE")),
                                // safety on digits
                                new Transition<>(0, 0, GO_LEFT, FB(a, b, "WRITE")),
                                new Transition<>(1, 1, GO_LEFT, FB(a, b, "WRITE")),
                                new Transition<>(2, 2, GO_LEFT, FB(a, b, "WRITE")),
                                new Transition<>(3, 3, GO_LEFT, FB(a, b, "WRITE")),
                                new Transition<>(4, 4, GO_LEFT, FB(a, b, "WRITE")),
                                new Transition<>(5, 5, GO_LEFT, FB(a, b, "WRITE")),
                                new Transition<>(6, 6, GO_LEFT, FB(a, b, "WRITE")),
                                new Transition<>(7, 7, GO_LEFT, FB(a, b, "WRITE")),
                                new Transition<>(8, 8, GO_LEFT, FB(a, b, "WRITE")),
                                new Transition<>(9, 9, GO_LEFT, FB(a, b, "WRITE"))
                        )
                ));
            }

            // WRITE: write q = (b==0 ? 0 : floor(a/b)) at A and STOP
            for (int b = 0; b <= 9; b++) {
                int q = (b == 0) ? 0 : (a / b);
                tables.add(new TransitionTable<>(
                        FB(a, b, "WRITE"),
                        asList(
                                new Transition<>(0, q, STOP, FB(a, b, "WRITE")),
                                new Transition<>(1, q, STOP, FB(a, b, "WRITE")),
                                new Transition<>(2, q, STOP, FB(a, b, "WRITE")),
                                new Transition<>(3, q, STOP, FB(a, b, "WRITE")),
                                new Transition<>(4, q, STOP, FB(a, b, "WRITE")),
                                new Transition<>(5, q, STOP, FB(a, b, "WRITE")),
                                new Transition<>(6, q, STOP, FB(a, b, "WRITE")),
                                new Transition<>(7, q, STOP, FB(a, b, "WRITE")),
                                new Transition<>(8, q, STOP, FB(a, b, "WRITE")),
                                new Transition<>(9, q, STOP, FB(a, b, "WRITE"))
                        )
                ));
            }
        }

        return tables;
    }
}
