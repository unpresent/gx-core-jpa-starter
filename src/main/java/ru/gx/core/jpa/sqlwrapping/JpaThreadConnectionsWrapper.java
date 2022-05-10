package ru.gx.core.jpa.sqlwrapping;

import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.gx.core.data.sqlwrapping.ConnectionWrapper;
import ru.gx.core.data.sqlwrapping.ThreadConnectionsWrapper;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;
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
    private final Map<Thread, JpaConnectionWrapper> connections = new HashMap<>();

    public JpaThreadConnectionsWrapper(@NotNull EntityManagerFactory entityManagerFactory) {
        final var unwrap = entityManagerFactory.unwrap(SessionFactory.class);
        if (unwrap == null) {
            throw new NullPointerException("entityManagerFactory is not a hibernate factory!");
        }
        this.sessionFactory = unwrap;
    }

    @Override
    @NotNull
    public ConnectionWrapper getCurrentThreadConnection() {
        var result = internalGet(Thread.currentThread());
        if (result != null) {
            result.incRefs();
        } else {
            result = new JpaConnectionWrapper(this.sessionFactory.openSession());
            internalPut(Thread.currentThread(), result);
        }
        return result;
    }

    @Override
    public void putCurrentThreadConnection(@NotNull ConnectionWrapper connectionWrapper) {
        internalPut(Thread.currentThread(), (JpaConnectionWrapper) connectionWrapper);
    }

    @Override
    public void clearCurrentThreadConnection() {
        internalRemove(Thread.currentThread());
    }

    @Nullable
    protected synchronized JpaConnectionWrapper internalGet(@NotNull final Thread thread) {
        return this.connections.get(thread);
    }

    protected synchronized void internalPut(
            @NotNull final Thread thread,
            @NotNull final JpaConnectionWrapper connectionWrapper
    ) {
        final var oldWrapper = this.connections.get(thread);
        if (oldWrapper != null && !oldWrapper.equals(connectionWrapper)) {
            oldWrapper.close();
        }
        this.connections.put(thread, connectionWrapper);
    }

    protected synchronized void internalRemove(@NotNull final Thread thread) {
        final var oldWrapper = this.connections.get(thread);
        if (oldWrapper != null) {
            oldWrapper.close();
        }
        this.connections.remove(thread);
    }

}
