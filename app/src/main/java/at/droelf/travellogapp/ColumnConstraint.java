package at.droelf.travellogapp;

public enum ColumnConstraint {
    PRIMARY_KEY("PRIMARY KEY");

    private final String text;

    private ColumnConstraint(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
