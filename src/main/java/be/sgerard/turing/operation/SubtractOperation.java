package be.sgerard.turing.operation;

import be.sgerard.turing.RegisterAction;
import be.sgerard.turing.Transition;
import be.sgerard.turing.TransitionTable;

import java.util.ArrayList;
import java.util.List;

import static be.sgerard.turing.RegisterAction.*;
import static java.util.Arrays.asList;

/**
 * Subtraction supporting two forms:
 * - Unary decrement: n -> max(n-1, 0)
 * - Binary subtraction on single digits: A - B -> max(A-B, 0) where tape is [A][null][B]
 *
 * Unary behavior keeps previous multi‑digit decrement with borrow across digits.
 * Binary behavior follows the same tape convention as SUM/MULTIPLY (single digits only at this stage).
 *
 * Strategy overview:
 * - READ_A: capture A and seek first null; CHECK_B to detect if binary (digit after null) or unary (null after null).
 * - If unary: position left of last digit and jump into TABLE_B (old decrement logic).
 * - If binary: at B position, null B, cross separator, write max(a-b,0) over A and STOP, leaving separator/B cleared.
 *
 * @author Jetbrains Junie
 */
public class SubtractOperation implements NumericOperation {

    // Unary decrement states (legacy)
    public static final String TABLE_A = "A"; // not used as entry anymore, kept for completeness
    public static final String TABLE_B = "B";
    public static final String TABLE_C = "C";
    public static final String TABLE_D = "D";
    public static final String TABLE_E = "E";

    // New entry / decision states
    private static final String READ_A = "READ_A";
    private static String F(int a, String suffix) { return "F" + a + "_" + suffix; }
    private static String FB(int a, int b, String suffix) { return "F" + a + "_B" + b + "_" + suffix; }

    @Override
    public NumericOperationName getName() {
        return NumericOperationName.SUBTRACT;
    }

    @Override
    public List<TransitionTable<Integer>> getTransitions() {
        final List<TransitionTable<Integer>> tables = new ArrayList<>();

        // ENTRY: READ_A — keep A as-is and start seeking first null
        final List<Transition<Integer>> readATransitions = new ArrayList<>();
        for (int a = 0; a <= 9; a++) {
            readATransitions.add(new Transition<>(a, a, GO_RIGHT, F(a, "SEEK_SEP")));
        }
        tables.add(new TransitionTable<>(READ_A, readATransitions));

        for (int a = 0; a <= 9; a++) {
            // SEEK_SEP: move right until a null, then move right into CHECK_B
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
                            new Transition<>(null, null, GO_RIGHT, F(a, "CHECK_B"))
                    )
            ));

            // CHECK_B: if digit -> binary, if null -> unary (end-of-number). If unary, we need to move back to last digit and go to TABLE_B.
            tables.add(new TransitionTable<>(
                    F(a, "CHECK_B"),
                    asList(
                            // Binary path for each possible B
                            new Transition<>(0, null, GO_LEFT, FB(a, 0, "LEFT1")),
                            new Transition<>(1, null, GO_LEFT, FB(a, 1, "LEFT1")),
                            new Transition<>(2, null, GO_LEFT, FB(a, 2, "LEFT1")),
                            new Transition<>(3, null, GO_LEFT, FB(a, 3, "LEFT1")),
                            new Transition<>(4, null, GO_LEFT, FB(a, 4, "LEFT1")),
                            new Transition<>(5, null, GO_LEFT, FB(a, 5, "LEFT1")),
                            new Transition<>(6, null, GO_LEFT, FB(a, 6, "LEFT1")),
                            new Transition<>(7, null, GO_LEFT, FB(a, 7, "LEFT1")),
                            new Transition<>(8, null, GO_LEFT, FB(a, 8, "LEFT1")),
                            new Transition<>(9, null, GO_LEFT, FB(a, 9, "LEFT1")),
                            // Unary path: null -> step left to separator, then another step left to last digit and jump to TABLE_B
                            new Transition<>(null, null, GO_LEFT, F(a, "UNARY_PREP"))
                    )
            ));

            // UNARY_PREP: currently on the separator position (null). Move left to last digit and enter TABLE_B.
            tables.add(new TransitionTable<>(
                    F(a, "UNARY_PREP"),
                    asList(
                            new Transition<>(null, null, GO_LEFT, TABLE_B),
                            // safety fallbacks
                            new Transition<>(0, 0, GO_LEFT, TABLE_B),
                            new Transition<>(1, 1, GO_LEFT, TABLE_B),
                            new Transition<>(2, 2, GO_LEFT, TABLE_B),
                            new Transition<>(3, 3, GO_LEFT, TABLE_B),
                            new Transition<>(4, 4, GO_LEFT, TABLE_B),
                            new Transition<>(5, 5, GO_LEFT, TABLE_B),
                            new Transition<>(6, 6, GO_LEFT, TABLE_B),
                            new Transition<>(7, 7, GO_LEFT, TABLE_B),
                            new Transition<>(8, 8, GO_LEFT, TABLE_B),
                            new Transition<>(9, 9, GO_LEFT, TABLE_B)
                    )
            ));

            // Binary path helpers
            // LEFT1: we are at separator (null) after erasing B; move left to A position
            tables.add(new TransitionTable<>(
                    FB(a, 0, "LEFT1"),
                    asList(
                            new Transition<>(null, null, GO_LEFT, FB(a, 0, "WRITE")),
                            new Transition<>(0, 0, GO_LEFT, FB(a, 0, "WRITE")),
                            new Transition<>(1, 1, GO_LEFT, FB(a, 0, "WRITE")),
                            new Transition<>(2, 2, GO_LEFT, FB(a, 0, "WRITE")),
                            new Transition<>(3, 3, GO_LEFT, FB(a, 0, "WRITE")),
                            new Transition<>(4, 4, GO_LEFT, FB(a, 0, "WRITE")),
                            new Transition<>(5, 5, GO_LEFT, FB(a, 0, "WRITE")),
                            new Transition<>(6, 6, GO_LEFT, FB(a, 0, "WRITE")),
                            new Transition<>(7, 7, GO_LEFT, FB(a, 0, "WRITE")),
                            new Transition<>(8, 8, GO_LEFT, FB(a, 0, "WRITE")),
                            new Transition<>(9, 9, GO_LEFT, FB(a, 0, "WRITE"))
                    )
            ));
            for (int b = 1; b <= 9; b++) {
                tables.add(new TransitionTable<>(
                        FB(a, b, "LEFT1"),
                        asList(
                                new Transition<>(null, null, GO_LEFT, FB(a, b, "WRITE")),
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

            // WRITE: now on A position; write max(a-b,0) and STOP
            for (int b = 0; b <= 9; b++) {
                int diff = a - b;
                int r = Math.max(diff, 0);
                tables.add(new TransitionTable<>(
                        FB(a, b, "WRITE"),
                        asList(
                                new Transition<>(0, r, STOP, FB(a, b, "WRITE")),
                                new Transition<>(1, r, STOP, FB(a, b, "WRITE")),
                                new Transition<>(2, r, STOP, FB(a, b, "WRITE")),
                                new Transition<>(3, r, STOP, FB(a, b, "WRITE")),
                                new Transition<>(4, r, STOP, FB(a, b, "WRITE")),
                                new Transition<>(5, r, STOP, FB(a, b, "WRITE")),
                                new Transition<>(6, r, STOP, FB(a, b, "WRITE")),
                                new Transition<>(7, r, STOP, FB(a, b, "WRITE")),
                                new Transition<>(8, r, STOP, FB(a, b, "WRITE")),
                                new Transition<>(9, r, STOP, FB(a, b, "WRITE"))
                        )
                ));
            }
        }

        // Legacy unary decrement machinery (borrow chain). Will be entered via UNARY_PREP.
        tables.add(new TransitionTable<>(
                TABLE_B,
                asList(
                        new Transition<>(0, 0, GO_LEFT, TABLE_C), // borrow chain
                        new Transition<>(1, 0, STOP, TABLE_B),
                        new Transition<>(2, 1, STOP, TABLE_B),
                        new Transition<>(3, 2, STOP, TABLE_B),
                        new Transition<>(4, 3, STOP, TABLE_B),
                        new Transition<>(5, 4, STOP, TABLE_B),
                        new Transition<>(6, 5, STOP, TABLE_B),
                        new Transition<>(7, 6, STOP, TABLE_B),
                        new Transition<>(8, 7, STOP, TABLE_B),
                        new Transition<>(9, 8, STOP, TABLE_B),
                        new Transition<>(null, 0, STOP, TABLE_B) // safety (shouldn't happen)
                )
        ));
        tables.add(new TransitionTable<>(
                TABLE_C,
                asList(
                        new Transition<>(null, null, GO_RIGHT, TABLE_E), // single zero case
                        new Transition<>(0, 0, GO_RIGHT, TABLE_D),
                        new Transition<>(1, 1, GO_RIGHT, TABLE_D),
                        new Transition<>(2, 2, GO_RIGHT, TABLE_D),
                        new Transition<>(3, 3, GO_RIGHT, TABLE_D),
                        new Transition<>(4, 4, GO_RIGHT, TABLE_D),
                        new Transition<>(5, 5, GO_RIGHT, TABLE_D),
                        new Transition<>(6, 6, GO_RIGHT, TABLE_D),
                        new Transition<>(7, 7, GO_RIGHT, TABLE_D),
                        new Transition<>(8, 8, GO_RIGHT, TABLE_D),
                        new Transition<>(9, 9, GO_RIGHT, TABLE_D)
                )
        ));
        tables.add(new TransitionTable<>(
                TABLE_D,
                asList(
                        new Transition<>(0, 9, GO_LEFT, TABLE_B),
                        new Transition<>(1, 9, GO_LEFT, TABLE_B), // should not occur, but safe fallback
                        new Transition<>(2, 9, GO_LEFT, TABLE_B),
                        new Transition<>(3, 9, GO_LEFT, TABLE_B),
                        new Transition<>(4, 9, GO_LEFT, TABLE_B),
                        new Transition<>(5, 9, GO_LEFT, TABLE_B),
                        new Transition<>(6, 9, GO_LEFT, TABLE_B),
                        new Transition<>(7, 9, GO_LEFT, TABLE_B),
                        new Transition<>(8, 9, GO_LEFT, TABLE_B),
                        new Transition<>(9, 9, GO_LEFT, TABLE_B),
                        new Transition<>(null, 9, GO_LEFT, TABLE_B) // safety
                )
        ));
        tables.add(new TransitionTable<>(
                TABLE_E,
                asList(
                        new Transition<>(0, 0, STOP, TABLE_E),
                        new Transition<>(null, null, STOP, TABLE_E)
                )
        ));

        return tables;
    }
}
