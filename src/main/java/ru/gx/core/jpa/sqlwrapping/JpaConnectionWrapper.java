package ru.gx.core.jpa.sqlwrapping;

import lombok.AccessLevel;
import lombok.Getter;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import ru.gx.core.data.sqlwrapping.ConnectionWrapper;
import ru.gx.core.data.sqlwrapping.SqlCommandWrapper;

import java.sql.SQLException;

import static lombok.AccessLevel.PROTECTED;

public class JpaConnectionWrapper implements ConnectionWrapper {

    @Getter
    @NotNull
    private final JpaThreadConnectionsWrapper owner;

    @Getter(PROTECTED)
    @NotNull
    private final Session session;

    /**
     * Определяет количество использований данного объекта.
     * Когда оно становится равно 0, то вызываем close() у внутреннего session.
     */
    private int refsCount = 1;

    public JpaConnectionWrapper(
            @NotNull final JpaThreadConnectionsWrapper owner,
            @NotNull final Session session
    ) {
        this.owner = owner;
        this.session = session;
    }

    @Override
    public @NotNull Object getInternalConnection() {
        return getSession();
    }

    @Override
    public @NotNull SqlCommandWrapper getQuery(@NotNull final String sql) {
        return new JpaQueryWrapper(getSession().createNativeQuery(sql), this);
    }

    @Override
    public @NotNull SqlCommandWrapper getCallable(@NotNull String sql) {
        return new JpaCallableWrapper(getSession().createNativeQuery(sql), this);
    }

    @Override
    public void openTransaction() {
        getSession().beginTransaction();
    }

    @Override
    public void commitTransaction() {
        getSession().getTransaction().commit();
    }

    @Override
    public void rollbackTransaction() {
        getSession().getTransaction().rollback();
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isTransactionOpened() {
        return getSession().getTransaction() != null;
    }

    public void incRefs() {
        this.refsCount++;
    }

    @Override
    public void close() {
        this.refsCount--;
        if (this.refsCount <= 0) {
            getOwner().internalRemove(Thread.currentThread(), this);
            getSession().close();
        }
    }
}
