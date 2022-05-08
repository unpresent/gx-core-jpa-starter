package ru.gx.core.jpa.save;

import org.jetbrains.annotations.NotNull;
import ru.gx.core.data.save.AbstractDbSavingDescriptor;
import ru.gx.core.data.save.DbSavingOperator;

public class JpaDbSavingDescriptor extends AbstractDbSavingDescriptor {
    public JpaDbSavingDescriptor(@NotNull DbSavingOperator saveOperator) {
        super(saveOperator);
    }
}
