package com.bigprime.db.ddl.internals;

import com.bigprime.db.DataBaseAbstract;
import com.bigprime.db.ddl.interfaces.IColumn;
import com.bigprime.db.ddl.model.ColumnBase;
import org.springframework.stereotype.Component;

/**
 * @author lyw
 * @version 1.0
 */
@Component
public abstract class ColumnAbstract<T extends ColumnBase> extends DataBaseAbstract implements IColumn<T> {
}
