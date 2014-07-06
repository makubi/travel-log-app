package at.droelf.travellogapp;

public enum ColumnType {
    TEXT("TEXT"), INTEGER("INTEGER");

    private final String text;

    private ColumnType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
