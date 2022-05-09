package ru.gx.core.jpa.sqlwrapping;

import lombok.AccessLevel;
import lombok.Getter;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import ru.gx.core.data.sqlwrapping.ConnectionWrapper;
import ru.gx.core.data.sqlwrapping.SqlCommandWrapper;

public class JpaConnectionWrapper implements ConnectionWrapper {
    @Getter(AccessLevel.PROTECTED)
    @NotNull
    private final Session session;

    /**
     * Определяет количество использований данного объекта.
     * Когда оно становится равно 0, то вызываем close() у внутреннего session.
     */
    private int refsCount = 1;

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

    public void incRefs() {
        this.refsCount++;
    }

    @Override
    public void close() {
        this.refsCount--;
        if (this.refsCount <= 0) {
            getSession().close();
        }
    }
}
