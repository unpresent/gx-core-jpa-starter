package ru.gx.core.jpa.sqlwrapping;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.gx.core.data.sqlwrapping.ConnectionWrapper;
import ru.gx.core.data.sqlwrapping.ThreadConnectionsWrapper;

import javax.persistence.EntityManagerFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Требуется для работы с транзакциями.
 */
@SuppressWarnings("unused")
public class JpaThreadConnectionsWrapper implements ThreadConnectionsWrapper {
    @NotNull
    private final SessionFactory sessionFactory;

    @NotNull
    private final Map<Thread, Session> sessions = new HashMap<>();

    public JpaThreadConnectionsWrapper(@NotNull EntityManagerFactory entityManagerFactory) {
        final var unwrap = entityManagerFactory.unwrap(SessionFactory.class);
        if (unwrap == null) {
            throw new NullPointerException("entityManagerFactory is not a hibernate factory!");
        }
        this.sessionFactory = unwrap;
    }

    @Nullable
    public synchronized Session get(@NotNull final Thread thread) {
        return this.sessions.get(thread);
    }

    @NotNull
    public Session getCurrent() throws SQLException {
        final var result = get(Thread.currentThread());
        return (result != null) ?  result : this.sessionFactory.openSession();
    }

    public synchronized void put(@NotNull final Thread thread, @NotNull final Session session) {
        this.sessions.put(thread, session);
    }

    public synchronized void clear(@NotNull final Thread thread) {
        this.sessions.remove(thread);
    }

    public void putCurrent(@NotNull final Session session) {
        this.put(Thread.currentThread(), session);
    }

    public void clearCurrent() {
        this.clear(Thread.currentThread());
    }

    @Override
    @NotNull
    public ConnectionWrapper getCurrentThreadConnection() throws SQLException {
        return new JpaConnectionWrapper(getCurrent());
    }

    @Override
    public void putCurrentThreadConnection(@NotNull ConnectionWrapper connectionWrapper) {
        putCurrent((Session) connectionWrapper.getInternalConnection());
    }

    @Override
    public void clearCurrentThreadConnection() {
        clearCurrent();
    }
}
