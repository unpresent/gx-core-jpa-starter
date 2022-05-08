package ru.gx.core.jpa.sqlwrapping;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import ru.gx.core.data.sqlwrapping.ConnectionWrapper;
import ru.gx.core.data.sqlwrapping.SqlCommandWrapper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class JpaConnectionWrapper implements ConnectionWrapper {
    @Getter(AccessLevel.PROTECTED)
    @NotNull
    private final Session session;

    public JpaConnectionWrapper(@NotNull final Session session) {
        this.session = session;
    }

    @Override
    public @NotNull Object getInternalConnection() {
        return getSession();
    }

    @Override
    public @NotNull SqlCommandWrapper getQuery(@NotNull final String sql) {
        return new JpaQueryWrapper(getSession().getNamedNativeQuery(sql));
    }

    @Override
    public @NotNull SqlCommandWrapper getCallable(@NotNull String sql) {
        return new JpaCallableWrapper(getSession().createStoredProcedureCall(sql));
    }

    @Override
    public void close() {
        getSession().close();
    }
}
