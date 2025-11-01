package be.sgerard.turing;

import be.sgerard.turing.operation.NumericOperationName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ComputerTest {

    private final Computer computer = new Computer();

    @DisplayName("Sum")
    @Nested
    class Sum {

        @Test
        void sum10() {
            final int actual = computer.compute(10, NumericOperationName.SUM);

            assertEquals(11, actual);
        }

        @Test
        void sum0() {
            final int actual = computer.compute(0, NumericOperationName.SUM);

            assertEquals(1, actual);
        }

    }

    @DisplayName("Subtract")
    @Nested
    class Subtract {

        @Test
        void subtract10() {
            final int actual = computer.compute(10, NumericOperationName.SUBTRACT);

            assertEquals(9, actual);
        }

        @Test
        void subtract1() {
            final int actual = computer.compute(1, NumericOperationName.SUBTRACT);

            assertEquals(0, actual);
        }

        @Test
        void subtract0() {
            final int actual = computer.compute(0, NumericOperationName.SUBTRACT);

            assertEquals(0, actual);
        }

        @Test
        void subtract1000() {
            final int actual = computer.compute(1000, NumericOperationName.SUBTRACT);

            assertEquals(999, actual);
        }
    }
}

