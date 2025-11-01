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
        void sum7plus8() {
            final int actual = computer.compute(7, 8, NumericOperationName.SUM);
            assertEquals(15, actual);
        }

        @Test
        void sum9plus9() {
            final int actual = computer.compute(9, 9, NumericOperationName.SUM);
            assertEquals(18, actual);
        }

        @Test
        void sum0plus5() {
            final int actual = computer.compute(0, 5, NumericOperationName.SUM);
            assertEquals(5, actual);
        }

        @Test
        void sum5plus0() {
            final int actual = computer.compute(5, 0, NumericOperationName.SUM);
            assertEquals(5, actual);
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

    @DisplayName("Multiply")
    @Nested
    class MultiplySingleDigit {

        @Test
        void mul7x8() {
            final int actual = computer.compute(7, 8, NumericOperationName.MULTIPLY);
            assertEquals(56, actual);
        }

        @Test
        void mul9x9() {
            final int actual = computer.compute(9, 9, NumericOperationName.MULTIPLY);
            assertEquals(81, actual);
        }

        @Test
        void mul0x5() {
            final int actual = computer.compute(0, 5, NumericOperationName.MULTIPLY);
            assertEquals(0, actual);
        }

        @Test
        void mul1x7() {
            final int actual = computer.compute(1, 7, NumericOperationName.MULTIPLY);
            assertEquals(7, actual);
        }
    }
}

