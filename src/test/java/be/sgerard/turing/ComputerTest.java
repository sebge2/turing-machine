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
}

