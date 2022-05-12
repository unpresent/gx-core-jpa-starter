package ru.gx.core.jpa.save;

import org.jetbrains.annotations.NotNull;
import ru.gx.core.data.save.AbstractDbSaver;
import ru.gx.core.data.save.DbSavingConfiguration;

import java.util.List;

public class JpaDbSaver extends AbstractDbSaver {
    public JpaDbSaver(@NotNull final List<DbSavingConfiguration> configurations) {
        super(configurations);
    }
}
