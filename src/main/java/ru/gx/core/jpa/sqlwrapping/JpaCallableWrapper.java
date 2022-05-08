package ru.gx.core.jpa.sqlwrapping;

import lombok.Getter;
import org.hibernate.procedure.ProcedureCall;
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
    private final ProcedureCall procedureCall;

    public JpaCallableWrapper(@NotNull final ProcedureCall procedureCall) {
        this.procedureCall = procedureCall;
    }

    @Override
    public @NotNull Object getInternalCommand() {
        return getProcedureCall();
    }

    @Override
    public void setStringParam(int paramIndex, @Nullable String value) throws SQLException {
        getProcedureCall().setParameter(paramIndex, value);
    }

    @Override
    public void setIntegerParam(int paramIndex, @Nullable Integer value) throws SQLException {
        getProcedureCall().setParameter(paramIndex, value);
    }

    @Override
    public void setLongParam(int paramIndex, @Nullable Long value) throws SQLException {
        getProcedureCall().setParameter(paramIndex, value);
    }

    @Override
    public void setNumericParam(int paramIndex, @Nullable BigDecimal value)  throws SQLException {
        getProcedureCall().setParameter(paramIndex, value);
    }

    @Override
    public void executeNoResult() {
        getProcedureCall().execute();
    }

    @SuppressWarnings("unchecked")
    @Override
    public ResultWrapper executeWithResult() {
        return new JpaResultWrapper(getProcedureCall().getResultList());
    }

    @Override
    public void close() {
    }
}
