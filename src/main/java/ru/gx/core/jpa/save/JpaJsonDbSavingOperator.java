package ru.gx.core.jpa.save;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.hibernate.procedure.ProcedureCall;
import org.jetbrains.annotations.NotNull;
import ru.gx.core.data.save.AbstractJsonDbSavingOperator;
import ru.gx.core.data.save.DbSavingAccumulateMode;
import ru.gx.core.jpa.sqlwrapping.JpaThreadConnectionsWrapper;

import java.sql.SQLException;

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
    ) throws SQLException {
        var session = getThreadConnectionsWrapper().getCurrent();
        return session.createStoredProcedureCall(sqlCommand);
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
