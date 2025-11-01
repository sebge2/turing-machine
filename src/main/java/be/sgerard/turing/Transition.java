package be.sgerard.turing;

public record Transition<S>(S original,
                            S target,
                            RegisterAction registerAction,
                            String nextTable) {
}
