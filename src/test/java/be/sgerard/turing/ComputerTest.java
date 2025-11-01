package be.sgerard.turing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static be.sgerard.turing.operation.numeric.NumericOperationUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ComputerTest {

    private final Computer computer = new Computer();

    @DisplayName("Sum")
    @Nested
    class Sum {

        @Test
        void sum7plus8() {
            final int actual = computer.compute(add(7, 8));
            assertEquals(15, actual);
        }

        @Test
        void sum9plus9() {
            final int actual = computer.compute(add(9, 9));
            assertEquals(18, actual);
        }

        @Test
        void sum0plus5() {
            final int actual = computer.compute(add(0, 5));
            assertEquals(5, actual);
        }

        @Test
        void sum5plus0() {
            final int actual = computer.compute(add(5, 0));
            assertEquals(5, actual);
        }
    }

    @DisplayName("Subtract")
    @Nested
    class Subtract {

        @Test
        void subtract10() {
            final int actual = computer.compute(minus(10, 1));

            assertEquals(9, actual);
        }

        @Test
        void subtract1() {
            final int actual = computer.compute(minus(1, 1));

            assertEquals(0, actual);
        }

        @Test
        void subtract0() {
            final int actual = computer.compute(minus(0, 1));

            assertEquals(0, actual);
        }

        @Test
        void subtract1000() {
            final int actual = computer.compute(minus(1000, 1));

            assertEquals(999, actual);
        }

        @Test
        void subtract7minus3() {
            final int actual = computer.compute(minus(7, 3));
            assertEquals(4, actual);
        }

        @Test
        void subtract3minus7FloorsTo0() {
            final int actual = computer.compute(minus(3, 7));
            assertEquals(0, actual);
        }

        @Test
        void subtract9minus9() {
            final int actual = computer.compute(minus(9, 9));
            assertEquals(0, actual);
        }

        @Test
        void subtract0minus5FloorsTo0() {
            final int actual = computer.compute(minus(0, 5));
            assertEquals(0, actual);
        }

        @Test
        void subtract5minus0() {
            final int actual = computer.compute(minus(5, 0));
            assertEquals(5, actual);
        }
    }

    @DisplayName("Multiply")
    @Nested
    class MultiplySingleDigit {

        @Test
        void mul7x8() {
            final int actual = computer.compute(multiply(7, 8));
            assertEquals(56, actual);
        }

        @Test
        void mul9x9() {
            final int actual = computer.compute(multiply(9, 9));
            assertEquals(81, actual);
        }

        @Test
        void mul0x5() {
            final int actual = computer.compute(multiply(0, 5));
            assertEquals(0, actual);
        }

        @Test
        void mul1x7() {
            final int actual = computer.compute(multiply(1, 7));
            assertEquals(7, actual);
        }
    }

    @DisplayName("Divide")
    @Nested
    class DivideSingleDigit {

        @Test
        void div8by2() {
            final int actual = computer.compute(divide(8, 2));
            assertEquals(4, actual);
        }

        @Test
        void div7by3FloorsTo2() {
            final int actual = computer.compute(divide(7, 3));
            assertEquals(2, actual);
        }

        @Test
        void div0by5Is0() {
            final int actual = computer.compute(divide(0, 5));
            assertEquals(0, actual);
        }

        @Test
        void div5by1Is5() {
            final int actual = computer.compute(divide(5, 1));
            assertEquals(5, actual);
        }
    }
}

