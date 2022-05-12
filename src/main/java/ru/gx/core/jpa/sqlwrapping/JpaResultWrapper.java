package ru.gx.core.jpa.sqlwrapping;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.gx.core.data.sqlwrapping.ResultWrapper;

import java.math.BigDecimal;
import java.math.BigInteger;
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
        this.currentIndex = -1;
    }

    @Override
    public @NotNull Object getInternalData() {
        return getResultSet();
    }

    @Override
    public boolean next() {
        currentIndex++;
        if (currentIndex > this.resultSet.size()) {
            currentIndex = this.resultSet.size();
            return false;
        }
        return currentIndex < this.resultSet.size();
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
        return (String) (getResultSet().get(currentIndex)[columnIndex-1]);
    }

    @Override
    public @Nullable Integer getInteger(int columnIndex) {
        return (Integer) (getResultSet().get(currentIndex)[columnIndex-1]);
    }

    @Override
    public @Nullable Long getLong(int columnIndex) {
        final var value = (getResultSet().get(currentIndex)[columnIndex-1]);
        if (value == null) {
            throw new NullPointerException("Unable cast null to Long for column " + columnIndex + "!");
        }
        if (value instanceof final BigInteger bigInteger) {
            return bigInteger.longValue();
        } else if (value instanceof final Integer integer) {
            return (long)integer;
        } else {
            throw new ClassCastException("Unable cast value " + value + " to Long for column " + columnIndex + "!");
        }
    }

    @Override
    public @Nullable BigDecimal getNumeric(int columnIndex) {
        return (BigDecimal) (getResultSet().get(currentIndex)[columnIndex-1]);
    }
}
