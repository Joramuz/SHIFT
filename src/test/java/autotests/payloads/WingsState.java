package autotests.payloads;

public enum WingsState {
    ACTIVE("ACTIVE"),
    FIXED("FIXED"),
    UNDEFINED("UNDEFINED");
    private final String state;

    WingsState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return state;
    }
}