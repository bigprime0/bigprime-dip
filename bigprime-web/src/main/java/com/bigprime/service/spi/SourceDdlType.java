package com.bigprime.service.spi;

import com.bigprime.plugin.manager.Plugin;
import com.bigprime.plugin.manager.constant.PluginType;
import com.bigprime.plugin.manager.internals.impl.SourcePlugin;
import com.bigprime.query.spi.SourceDdlQuery;

/**
 * @author lyw
 * @version 1.0
 */
public enum SourceDdlType {
    getDatabases {
        @Override
        public Object execute(SourceDdlQuery query) {
            return Plugin.<SourcePlugin>getInstance(PluginType.SOURCE, query.getDatabaseId()).getDatabases();
        }
    },
    getTables {
        @Override
        public Object execute(SourceDdlQuery query) {
            return Plugin.<SourcePlugin>getInstance(PluginType.SOURCE, query.getDatabaseId()).getTables(query.getIsCascade());
        }
    },
    getTable {
        @Override
        public Object execute(SourceDdlQuery query) {
            return Plugin.<SourcePlugin>getInstance(PluginType.SOURCE, query.getDatabaseId()).getTable(query.getName());
        }
    },
    getCreateStatement {
        @Override
        public Object execute(SourceDdlQuery query) {
            return Plugin.<SourcePlugin>getInstance(PluginType.SOURCE, query.getDatabaseId()).getCreateStatement(query.getName());
        }
    },
    getViews {
        @Override
        public Object execute(SourceDdlQuery query) {
            return Plugin.<SourcePlugin>getInstance(PluginType.SOURCE, query.getDatabaseId()).getViews();
        }
    },
    getView {
        @Override
        public Object execute(SourceDdlQuery query) {
            return Plugin.<SourcePlugin>getInstance(PluginType.SOURCE, query.getDatabaseId()).getView(query.getName());
        }
    },
    getFunctions {
        @Override
        public Object execute(SourceDdlQuery query) {
            return Plugin.<SourcePlugin>getInstance(PluginType.SOURCE, query.getDatabaseId()).getFunctions();
        }
    },
    getFunction {
        @Override
        public Object execute(SourceDdlQuery query) {
            return Plugin.<SourcePlugin>getInstance(PluginType.SOURCE, query.getDatabaseId()).getFunction(query.getName());
        }
    },
    createTable {
        @Override
        public Object execute(SourceDdlQuery query) {
            return Plugin.<SourcePlugin>getInstance(PluginType.SOURCE, query.getDatabaseId()).createTable(query.getModel());
        }
    },
    alterTable {
        @Override
        public Object execute(SourceDdlQuery query) {
            return Plugin.<SourcePlugin>getInstance(PluginType.SOURCE, query.getDatabaseId()).alterTable(query.getModel());
        }
    },
    createColumn {
        @Override
        public Object execute(SourceDdlQuery query) {
            return Plugin.<SourcePlugin>getInstance(PluginType.SOURCE, query.getDatabaseId()).createColumn(query.getName(), query.getColumnModel());
        }
    },
    alterColumn {
        @Override
        public Object execute(SourceDdlQuery query) {
            return Plugin.<SourcePlugin>getInstance(PluginType.SOURCE, query.getDatabaseId()).alterColumn(query.getName(), query.getColumnModel());
        }
    },
    dropTable {
        @Override
        public Object execute(SourceDdlQuery query) {
            return Plugin.<SourcePlugin>getInstance(PluginType.SOURCE, query.getDatabaseId()).dropTable(query.getName());
        }
    },
    dropColumn {
        @Override
        public Object execute(SourceDdlQuery query) {
            return Plugin.<SourcePlugin>getInstance(PluginType.SOURCE, query.getDatabaseId()).dropColumn(query.getName(), query.getColumnName());
        }
    }
    ;

    public abstract Object execute(SourceDdlQuery query);
}
