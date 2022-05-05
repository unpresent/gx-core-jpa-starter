package ru.gx.core.jpa.save;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.hibernate.procedure.ProcedureCall;
import org.jetbrains.annotations.NotNull;
import ru.gx.core.data.save.AbstractBinaryDbSavingOperator;
import ru.gx.core.data.save.DbSavingAccumulateMode;
import ru.gx.core.jpa.ActiveSessionsContainer;

import java.sql.SQLException;

@Accessors(chain = true)
public class JdbcBinaryDbSavingOperator
        extends AbstractBinaryDbSavingOperator {

    @Getter(AccessLevel.PROTECTED)
    @NotNull
    private final ActiveSessionsContainer activeSessionsContainer;

    public JdbcBinaryDbSavingOperator(
            @NotNull final ObjectMapper objectMapper,
            @NotNull final ActiveSessionsContainer activeSessionsContainer
    ) {
        super(objectMapper);
        this.activeSessionsContainer = activeSessionsContainer;
    }

    @Override
    public @NotNull ProcedureCall prepareStatement(
            @NotNull final String sqlCommand,
            @NotNull final DbSavingAccumulateMode accumulateMode
    ) throws SQLException {
        var session = getActiveSessionsContainer().getCurrent();
        if (session == null) {
            throw new SQLException("Session isn't registered in ActiveConnectionsContainer");
        }
        return session.getNamedProcedureCall(sqlCommand);
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
