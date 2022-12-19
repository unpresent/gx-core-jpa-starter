package ru.gx.core.jpa.save;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import ru.gx.core.data.save.AbstractJsonDbSavingOperator;
import ru.gx.core.data.save.DbSavingAccumulateMode;
import ru.gx.core.data.sqlwrapping.SqlCommandWrapper;
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
    public @NotNull SqlCommandWrapper prepareStatement(
            @NotNull final String sqlCommand,
            @NotNull final DbSavingAccumulateMode accumulateMode
    ) throws SQLException {
        var session = getThreadConnectionsWrapper().getCurrentThreadConnection();
        return session.getCallable(sqlCommand);
    }

    @Override
    protected void executeStatement(
            @NotNull final SqlCommandWrapper statement,
            @NotNull final Object data
    ) throws SQLException {
        statement.setStringParam(1, (String) data);
        statement.executeNoResult();
    }
}
