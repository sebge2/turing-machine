package be.sgerard.turing;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.unmodifiableList;

public record Register<S>(List<S> symbols, int cursor) {

    public static <S> Register<S> create(List<S> symbols) {
        return new Register<>(symbols, 0);
    }

    public Register(List<S> symbols, int cursor) {
        this.symbols = unmodifiableList(symbols);
        this.cursor = cursor;
    }

    public Optional<S> current() {
        if ((cursor < 0) || (cursor >= symbols.size())) {
            return Optional.empty();
        }

        final S value = symbols.get(cursor);
        return (value == null) ? Optional.empty() : Optional.of(value);
    }

    public Register<S> update(S newSymbol) {
        final List<S> updatedSymbols = new ArrayList<>(symbols);

        if (cursor < 0) {
            updatedSymbols.addFirst(newSymbol);
        } else if (cursor >= symbols.size()) {
            updatedSymbols.add(newSymbol);
        } else {
            updatedSymbols.set(cursor, newSymbol);
        }

        return new Register<>(updatedSymbols, cursor);
    }

    public Register<S> perform(RegisterAction action) {
        return switch (action) {
            case STOP -> this;
            case GO_LEFT -> new Register<>(symbols, cursor - 1);
            case GO_RIGHT -> new Register<>(symbols, cursor + 1);
        };
    }
}
