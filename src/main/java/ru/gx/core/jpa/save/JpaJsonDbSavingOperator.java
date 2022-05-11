package ru.gx.core.jpa.save;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.hibernate.Session;
import org.hibernate.procedure.ProcedureCall;
import org.jetbrains.annotations.NotNull;
import ru.gx.core.data.save.AbstractJsonDbSavingOperator;
import ru.gx.core.data.save.DbSavingAccumulateMode;
import ru.gx.core.jpa.sqlwrapping.JpaThreadConnectionsWrapper;

@Accessors(chain = true)
public class JpaJsonDbSavingOperator
        extends AbstractJsonDbSavingOperator {

    @Getter
    @NotNull
    private final JpaThreadConnectionsWrapper threadConnectionsWrapper;

    public JpaJsonDbSavingOperator(
            @NotNull final ObjectMapper objectMapper,
            @NotNull final JpaThreadConnectionsWrapper threadConnectionsWrapper
    ) {
        super(objectMapper);
        this.threadConnectionsWrapper = threadConnectionsWrapper;
    }

    @Override
    public @NotNull ProcedureCall prepareStatement(
            @NotNull final String sqlCommand,
            @NotNull final DbSavingAccumulateMode accumulateMode
    ) {
        var session = getThreadConnectionsWrapper().getCurrentThreadConnection();
        return ((Session)session.getInternalConnection()).getNamedProcedureCall(sqlCommand);
    }

    @Override
    protected void executeStatement(
            @NotNull final Object statement,
            @NotNull final Object data
    ) {
        final var stmt = (ProcedureCall)statement;
        stmt.setParameter(1, data);
        stmt.execute();
    }
}
