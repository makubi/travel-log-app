package at.droelf.travellogapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColumnStringBuilder {

    private final List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
    private final List<ColumnConstraintDefinition> columnConstraintDefinitions = new ArrayList<ColumnConstraintDefinition>();

    public ColumnStringBuilder addColumn(String name, ColumnType type) {
        columnDefinitions.add(new ColumnDefinition(name, type));

        return this;
    }

    public ColumnStringBuilder addConstraint(String name, ColumnConstraint columnConstraint) {
        addConstraint(new String[]{name}, columnConstraint);

        return this;
    }

    public ColumnStringBuilder addConstraint(String[] names, ColumnConstraint columnConstraint) {
        columnConstraintDefinitions.add(new ColumnConstraintDefinition(names, columnConstraint));

        return this;
    }

    class ColumnDefinition {
        final String name;
        final ColumnType columnType;

        ColumnDefinition(String name, ColumnType columnType) {
            this.name = name;
            this.columnType = columnType;
        }
    }

    class ColumnConstraintDefinition {
        final String[] columns;
        final ColumnConstraint columnConstraint;

        ColumnConstraintDefinition(String[] columns, ColumnConstraint columnConstraint) {
            this.columns = columns;
            this.columnConstraint = columnConstraint;
        }
    }

    public String build() {
        StringBuilder stringBuilder = new StringBuilder();

        for(ColumnDefinition columnDefinition : columnDefinitions) {
            stringBuilder.append(columnDefinition.name + " " + columnDefinition.columnType.getText() + ", ");
        }

        for(ColumnConstraintDefinition columnConstraintDefinition : columnConstraintDefinitions) {
            switch (columnConstraintDefinition.columnConstraint) {
                case PRIMARY_KEY:

                    stringBuilder.append(columnConstraintDefinition.columnConstraint.getText() + " (");

                    for(String column : columnConstraintDefinition.columns) {
                        stringBuilder.append(column + ", ");
                    }

                    stringBuilder = new StringBuilder(stringBuilder.substring(0, stringBuilder.length() - 2));

                    stringBuilder.append(")");
                    break;
                default:
                    throw new RuntimeException("Unknown constraint: " + columnConstraintDefinition.columnConstraint);
            }
        }

        final String trimmedString = stringBuilder.toString().trim();
        return trimmedString.endsWith(",") ? trimmedString.substring(0, trimmedString.length() - 1) : trimmedString;
    }
}
