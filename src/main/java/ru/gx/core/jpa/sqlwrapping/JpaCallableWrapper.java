package ru.gx.core.jpa.sqlwrapping;

import lombok.Getter;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.query.NativeQuery;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.gx.core.data.sqlwrapping.ResultWrapper;
import ru.gx.core.data.sqlwrapping.SqlCommandWrapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

public class JpaCallableWrapper implements SqlCommandWrapper {
    @Getter(PROTECTED)
    @NotNull
    private final NativeQuery<?> nativeQuery;

    @Getter
    @NotNull
    private final JpaConnectionWrapper connection;

    public JpaCallableWrapper(
            @NotNull final NativeQuery<?> nativeQuery,
            @NotNull final JpaConnectionWrapper connection
    ) {
        this.nativeQuery = nativeQuery;
        this.connection = connection;
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
    public void setBinaryParam(int paramIndex, byte[] value) {
        getNativeQuery().setParameter(paramIndex, value);
    }

    @Override
    public void executeNoResult() {
        var localTranOpened = false;
        if (!this.connection.isTransactionOpened()) {
            getConnection().openTransaction();
            localTranOpened = true;
        }
        try {
            getNativeQuery().executeUpdate();
            if (localTranOpened) {
                getConnection().commitTransaction();
            }
        } catch (Exception e) {
            if (localTranOpened) {
                getConnection().rollbackTransaction();
            }
        }
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
