package ru.gx.core.jpa.sqlwrapping;

import lombok.Getter;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.Type;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.gx.core.data.sqlwrapping.ResultWrapper;
import ru.gx.core.data.sqlwrapping.SqlCommandWrapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@SuppressWarnings("ClassCanBeRecord")
public class JpaQueryWrapper implements SqlCommandWrapper {
    @Getter(PROTECTED)
    @NotNull
    private final NativeQuery<?> nativeQuery;

    public JpaQueryWrapper(@NotNull final NativeQuery<?> nativeQuery) {
        this.nativeQuery = nativeQuery;
    }

    @Override
    public @NotNull Object getInternalCommand() {
        return getNativeQuery();
    }

    @Override
    public void setStringParam(int paramIndex, @Nullable String value) {
        getNativeQuery().setParameter(paramIndex, value);
    }

    @Override
    public void setIntegerParam(int paramIndex, @Nullable Integer value) {
        getNativeQuery().setParameter(paramIndex, value);
    }

    @Override
    public void setLongParam(int paramIndex, @Nullable Long value) {
        getNativeQuery().setParameter(paramIndex, value);
    }

    @Override
    public void setNumericParam(int paramIndex, @Nullable BigDecimal value) {
        getNativeQuery().setParameter(paramIndex, value);
    }

    @Override
    public void executeNoResult() {
        getNativeQuery().executeUpdate();
    }

    @SuppressWarnings("unchecked")
    @Override
    public ResultWrapper executeWithResult() {
        return new JpaResultWrapper((List<Object[]>) getNativeQuery().getResultList());
    }

    @Override
    public void close() {
    }
}
