package com.bigprime.db.ddl.internals;

import com.bigprime.db.DataBaseAbstract;
import com.bigprime.db.ddl.interfaces.IIndex;
import com.bigprime.db.ddl.model.IndexBase;
import org.springframework.stereotype.Component;

/**
 * @author lyw
 * @version 1.0
 */
@Component
public abstract class IndexAbstract<T  extends IndexBase> extends DataBaseAbstract implements IIndex<T> {
}
