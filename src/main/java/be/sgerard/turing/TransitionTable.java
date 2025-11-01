package be.sgerard.turing;

import java.util.List;
import java.util.Objects;

public record TransitionTable<S>(String name,
                                 List<Transition<S>> symbols) {
    public Transition<S> getTransition(S symbol) {
        return symbols().stream()
                .filter(transition -> Objects.equals(transition.original(), symbol))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No transition for symbol [" + symbol + "]."));
    }
}
