package ru.gx.core.jpa.sqlwrapping;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.gx.core.data.sqlwrapping.ResultWrapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

public class JpaResultWrapper implements ResultWrapper {
    @Getter(PROTECTED)
    @NotNull
    private final List<Object[]> resultSet;

    private int currentIndex;

    public JpaResultWrapper(@NotNull final List<Object[]> resultSet) {
        this.resultSet = resultSet;
        this.currentIndex = 0;
    }

    @Override
    public @NotNull Object getInternalData() {
        return getResultSet();
    }

    @Override
    public boolean next() {
        if (currentIndex < this.resultSet.size()) {
            currentIndex++;
            return true;
        }
        return false;
    }

    @Override
    public boolean first() {
        if (this.resultSet.size() > 0) {
            this.currentIndex = 0;
            return true;
        } else {
            this.currentIndex = -1;
            return false;
        }
    }

    @Override
    public boolean last() {
        this.currentIndex = this.resultSet.size() - 1;
        return (this.currentIndex >= 0);
    }

    @Override
    public @Nullable String getString(int columnIndex) {
        return (String) (getResultSet().get(currentIndex)[columnIndex]);
    }

    @Override
    public @Nullable Integer getInteger(int columnIndex) {
        return (Integer) (getResultSet().get(currentIndex)[columnIndex]);
    }

    @Override
    public @Nullable Long getLong(int columnIndex) {
        return (Long) (getResultSet().get(currentIndex)[columnIndex]);
    }

    @Override
    public @Nullable BigDecimal getNumeric(int columnIndex) {
        return (BigDecimal) (getResultSet().get(currentIndex)[columnIndex]);
    }
}
