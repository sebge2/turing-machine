package be.sgerard.turing.operation;

import be.sgerard.turing.Transition;
import be.sgerard.turing.TransitionTable;

import java.util.List;

import static be.sgerard.turing.RegisterAction.*;
import static java.util.Arrays.asList;

/**
 * Decrement numeric value by 1 (floor at 0).
 *
 * Strategy (decimal):
 * - A: move cursor to the blank after the last digit, then go left to B.
 * - B: if digit is 1..9 -> write (digit-1) and STOP.
 *      if digit is 0 -> try to borrow: go to C (one step left) and check if there is a left neighbor.
 * - C: if there is no left neighbor (null) -> move right to E and STOP with 0 (so single 0 -> 0). Otherwise go right to D.
 * - D: we're back on the 0 we inspected from C -> write 9, go left, and continue in B.
 * - E: finalize single-zero case (stay 0 and STOP).
 *
 * @author Jetbrains Junie
 */
public class SubtractOperation implements NumericOperation {

    public static final String TABLE_A = "A";
    public static final String TABLE_B = "B";
    public static final String TABLE_C = "C";
    public static final String TABLE_D = "D";
    public static final String TABLE_E = "E";

    @Override
    public NumericOperationName getName() {
        return NumericOperationName.SUBTRACT;
    }

    @Override
    public List<TransitionTable<Integer>> getTransitions() {
        return List.of(
                // A: move to the end (blank), then step back to last digit (B)
                new TransitionTable<>(
                        TABLE_A,
                        asList(
                                new Transition<>(0, 0, GO_RIGHT, TABLE_A),
                                new Transition<>(1, 1, GO_RIGHT, TABLE_A),
                                new Transition<>(2, 2, GO_RIGHT, TABLE_A),
                                new Transition<>(3, 3, GO_RIGHT, TABLE_A),
                                new Transition<>(4, 4, GO_RIGHT, TABLE_A),
                                new Transition<>(5, 5, GO_RIGHT, TABLE_A),
                                new Transition<>(6, 6, GO_RIGHT, TABLE_A),
                                new Transition<>(7, 7, GO_RIGHT, TABLE_A),
                                new Transition<>(8, 8, GO_RIGHT, TABLE_A),
                                new Transition<>(9, 9, GO_RIGHT, TABLE_A),
                                new Transition<>(null, null, GO_LEFT, TABLE_B)
                        )
                ),
                // B: perform decrement or start borrow if digit is 0
                new TransitionTable<>(
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
                ),
                // C: after moving left from a trailing 0; if no more digits -> go right to E; else go right to D to set 9
                new TransitionTable<>(
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
                ),
                // D: set the inspected 0 to 9 and go back left to continue the borrow chain
                new TransitionTable<>(
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
                ),
                // E: finalize single-zero case (stay on 0 and STOP)
                new TransitionTable<>(
                        TABLE_E,
                        asList(
                                new Transition<>(0, 0, STOP, TABLE_E),
                                new Transition<>(null, null, STOP, TABLE_E)
                        )
                )
        );
    }
}
