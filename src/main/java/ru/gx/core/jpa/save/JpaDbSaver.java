package ru.gx.core.jpa.save;

import org.jetbrains.annotations.NotNull;
import ru.gx.core.data.save.AbstractDbSaver;
import ru.gx.core.data.save.DbSavingDescriptor;
import ru.gx.core.data.save.DbSavingOperator;

public class JpaDbSaver extends AbstractDbSaver {

    public JpaDbSaver(@NotNull DbSavingOperator saveOperator) {
        super(saveOperator);
    }

    @Override
    @NotNull
    protected DbSavingDescriptor createDescriptor(@NotNull final DbSavingOperator saveOperator) {
        return new JpaDbSavingDescriptor(saveOperator);
    }
}
