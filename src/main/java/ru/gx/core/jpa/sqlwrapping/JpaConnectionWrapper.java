package ru.gx.core.jpa.sqlwrapping;

import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.TransactionException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.gx.core.data.sqlwrapping.ConnectionWrapper;
import ru.gx.core.data.sqlwrapping.SqlCommandWrapper;

import static lombok.AccessLevel.PROTECTED;

public class JpaConnectionWrapper implements ConnectionWrapper {

    @Getter
    @NotNull
    private final JpaThreadConnectionsWrapper owner;

    @Getter(PROTECTED)
    @NotNull
    private final Session session;

    @Nullable
    private Transaction transaction;

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
        this.transaction = getSession().beginTransaction();
    }

    @Override
    public void commitTransaction() {
        if (this.transaction == null) {
            throw new TransactionException("Transaction is not opened!");
        }
        this.transaction.commit();
        this.transaction = null;
    }

    @Override
    public void rollbackTransaction() {
        if (this.transaction == null) {
            throw new TransactionException("Transaction is not opened!");
        }
        this.transaction.rollback();
        this.transaction = null;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isTransactionOpened() {
        return this.transaction != null && this.transaction.isActive();
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

    @Override
    public boolean isEqual(@Nullable ConnectionWrapper connectionWrapper) {
        if (connectionWrapper == null) {
            return false;
        }
        return getInternalConnection().equals(connectionWrapper.getInternalConnection());
    }
}
