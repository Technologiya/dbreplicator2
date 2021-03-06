/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 Technologiya
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package ru.taximaxim.dbreplicator2.replica.strategies.replication.data;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import ru.taximaxim.dbreplicator2.jdbc.JdbcMetadata;
import ru.taximaxim.dbreplicator2.jdbc.QueryConstructors;
import ru.taximaxim.dbreplicator2.jdbc.StatementsHashMap;
import ru.taximaxim.dbreplicator2.model.TableModel;

/**
 * @author volodin_aa
 * 
 */
public class GenericDataService extends DataServiceSkeleton implements DataService {
    /**
     * Кешированные запросы удаления данных в приемнике
     */
    private final StatementsHashMap<TableModel, PreparedStatement> deleteStatements = new StatementsHashMap<>();
    /**
     * Кешированные запросы получения данных из источника
     */
    private final StatementsHashMap<TableModel, PreparedStatement> selectStatements = new StatementsHashMap<>();
    /**
     * Кешированные запросы обновления данных в приемнике
     */
    private final StatementsHashMap<TableModel, PreparedStatement> updateStatements = new StatementsHashMap<>();
    /**
     * Кешировнные запросы вставки данных в приемник
     */
    private final StatementsHashMap<TableModel, PreparedStatement> insertStatements = new StatementsHashMap<>();

    private final Map<TableModel, Set<String>> priCols = new HashMap<>();
    private final Map<TableModel, Set<String>> allAvaliableCols = new HashMap<>();
    private final Map<TableModel, Set<String>> avaliableDataCols = new HashMap<>();
    private final Map<TableModel, Set<String>> allCols = new HashMap<>();
    private final Map<TableModel, Set<String>> dataCols = new HashMap<>();
    private final Map<TableModel, Set<String>> identityCols = new HashMap<>();
    private final Map<TableModel, Set<String>> ignoredCols = new HashMap<>();
    private final Map<TableModel, Set<String>> requiredCols = new HashMap<>();

    protected static final String WHERE = "where";
    
    private static final String KEYS = "keys";
    /**
     * 
     */
    public GenericDataService(DataSource dataSource) {
        super(dataSource);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ru.taximaxim.dbreplicator2.replica.DataService2#getDeleteStatement(ru.
     * taximaxim.dbreplicator2.model.TableModel)
     */
    @Override
    public PreparedStatement getDeleteStatement(TableModel table) throws SQLException {
        PreparedStatement statement = getDeleteStatements().get(table);
        if (statement == null) {
            statement = getConnection().prepareStatement(QueryConstructors
                    .constructDeleteQuery(table.getName(), getPriCols(table)));
            getDeleteStatements().put(table, statement);
        }

        return statement;
    }

    /**
     * @return the deleteStatements
     */
    protected Map<TableModel, PreparedStatement> getDeleteStatements() {
        return deleteStatements;
    }

    /**
     * Получение кеша запросов на выборку данных из источника
     * 
     * @return the selectStatements
     */
    protected Map<TableModel, PreparedStatement> getSelectStatements() {
        return selectStatements;
    }

    /**
     * @return the updateStatements
     */
    protected Map<TableModel, PreparedStatement> getUpdateStatements() {
        return updateStatements;
    }

    /**
     * Получение кеша запросов на вставку данных в приемник
     * 
     * @return the insertStatements
     */
    protected Map<TableModel, PreparedStatement> getInsertStatements() {
        return insertStatements;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ru.taximaxim.dbreplicator2.replica.DataService2#getSelectStatement(ru.
     * taximaxim.dbreplicator2.model.TableModel)
     */
    @Override
    public PreparedStatement getSelectStatement(TableModel table) throws SQLException {
        PreparedStatement statement = getSelectStatements().get(table);
        if (statement == null) {
            statement = getConnection().prepareStatement(
                    QueryConstructors.constructSelectQuery(table.getName(),
                            getAllCols(table), getPriCols(table), table.getParam(WHERE)));

            getSelectStatements().put(table, statement);
        }

        return statement;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ru.taximaxim.dbreplicator2.replica.DataService2#getUpdateStatement(ru.
     * taximaxim.dbreplicator2.model.TableModel)
     */
    @Override
    public PreparedStatement getUpdateStatement(TableModel table,
            Collection<String> avaliableCals) throws SQLException {
        PreparedStatement statement = getUpdateStatements().get(table);
        if (statement == null) {
            statement = getConnection().prepareStatement(
                    QueryConstructors.constructUpdateQuery(table.getName(),
                            getAvaliableDataCols(table, avaliableCals),
                            getPriCols(table)));

            getUpdateStatements().put(table, statement);
        }

        return statement;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ru.taximaxim.dbreplicator2.replica.DataService2#getInsertStatement(ru.
     * taximaxim.dbreplicator2.model.TableModel)
     */
    @Override
    public PreparedStatement getInsertStatement(TableModel table,
            Collection<String> avaliableCals) throws SQLException {
        PreparedStatement statement = getInsertStatements().get(table);
        if (statement == null) {
            String insertQuery = QueryConstructors.constructInsertQuery(table.getName(),
                    getAllAvaliableCols(table, avaliableCals));
            statement = getConnection().prepareStatement(insertQuery);

            getInsertStatements().put(table, statement);
        }

        return statement;
    }
    
    protected Collection<String> str2upperList(String str) {
        Collection<String> list = new ArrayList<>();
        if (str != null) {
            list = Arrays.asList(str.toUpperCase().split(","));
        }
        return list;
    }

    protected Collection<String> getKeysColumn(TableModel table) {
        return str2upperList(table.getParam(KEYS));
    }

    
    /**
     * Кешированное получение списка ключевых колонок
     * 
     * @param connection
     * @param table.getName()
     * @return
     * @throws SQLException
     */
    @Override
    public Set<String> getPriCols(TableModel table) throws SQLException {
        Set<String> cols = priCols.get(table);
        if (cols == null) {
            Collection<String>keys = getKeysColumn(table);
            if (keys.isEmpty()) {
                cols = JdbcMetadata.getPrimaryColumns(getConnection(), table.getName());
            } else {
                cols = new LinkedHashSet<>(keys);
            }
            priCols.put(table, cols);
        }

        return cols;
    }

    /**
     * Кешированное получение списка всех колонок
     * 
     * @param table.getName()
     * @return
     * @throws SQLException
     */
    @Override
    public Set<String> getAllCols(TableModel table) throws SQLException {
        Set<String> cols = allCols.get(table);
        if (cols == null) {
            cols = JdbcMetadata.getColumns(getConnection(), table.getName());

            // Удаляем игнорируемые колонки
            cols.removeAll(getIgnoredCols(table));

            // Оставляем обязательные колонки
            if (!getRequiredCols(table).isEmpty()) {
                cols.retainAll(getRequiredCols(table));
                cols.addAll(getPriCols(table));
            }

            allCols.put(table, cols);
        }

        return cols;
    }

    /**
     * Кешированное получение списка всех доступных колонок
     * 
     * @param table.getName()
     * @return
     * @throws SQLException
     */
    @Override
    public Set<String> getAllAvaliableCols(TableModel table,
            Collection<String> avaliableCals) throws SQLException {
        Set<String> cols = allAvaliableCols.get(table);
        if (cols == null) {
            cols = new LinkedHashSet<>(getAllCols(table));

            // Оставляем только доступные колонки
            cols.retainAll(avaliableCals);

            allAvaliableCols.put(table, cols);
        }

        return cols;
    }

    /**
     * Кешированное получение списка колонок с данными
     * 
     * @param connection
     * @param table.getName()
     * @return
     * @throws SQLException
     */
    @Override
    public Set<String> getDataCols(TableModel table) throws SQLException {
        Set<String> cols = dataCols.get(table);
        if (cols == null) {
            cols = new LinkedHashSet<>(getAllCols(table));
            cols.removeAll(getPriCols(table));

            dataCols.put(table, cols);
        }

        return cols;
    }

    /**
     * Кешированное получение списка доступных колонок с данными
     * 
     * @param connection
     * @param table.getName()
     * @return
     * @throws SQLException
     */
    @Override
    public Set<String> getAvaliableDataCols(TableModel table,
            Collection<String> avaliableCals) throws SQLException {
        Set<String> cols = avaliableDataCols.get(table);
        if (cols == null) {
            cols = new LinkedHashSet<>(getDataCols(table));

            // Оставляем только доступные колонки
            cols.retainAll(avaliableCals);
        }

        return cols;
    }

    /**
     * Кешированное получение списка автоинкрементных колонок
     * 
     * @param connection
     * @param table.getName()
     * @return
     * @throws SQLException
     */
    @Override
    public Set<String> getIdentityCols(TableModel table) throws SQLException {
        Set<String> cols = identityCols.get(table);
        if (cols == null) {
            cols = JdbcMetadata.getIdentityColumns(getConnection(), table.getName());
            identityCols.put(table, cols);
        }

        return cols;
    }

    /**
     * Кешированное получение списка игнорируемых колонок
     * 
     * @param connection
     * @param table.getName()
     * @return
     * @throws SQLException
     */
    @Override
    public Set<String> getIgnoredCols(TableModel table) throws SQLException {
        Set<String> cols = ignoredCols.get(table);
        if (cols == null) {
            cols = table.getIgnoredColumns();
            ignoredCols.put(table, cols);
        }

        return cols;
    }

    /**
     * Кешированное получение списка реплицуруеммых колонок
     * 
     * @param connection
     * @param table.getName()
     * @return
     * @throws SQLException
     */
    @Override
    public Set<String> getRequiredCols(TableModel table) throws SQLException {
        Set<String> cols = requiredCols.get(table);
        if (cols == null) {
            cols = table.getRequiredColumns();
            requiredCols.put(table, cols);
        }

        return cols;
    }

    /**
     * Устанавливает сессионную переменную с именем текущей подписки или
     * публикации
     * 
     * @throws Exception
     */
    @Override
    public void setRepServerName(String repServerName) throws SQLException {
        // По умолчанию ни чего не делаем. Реализуется для конкретных баз.
    }

    @Override
    public void close() throws SQLException {
        try (StatementsHashMap<TableModel, PreparedStatement> thisDeleteStatements = this.deleteStatements;
                StatementsHashMap<TableModel, PreparedStatement> thisSelectStatements = this.selectStatements;
                StatementsHashMap<TableModel, PreparedStatement> thisUpdateStatements = this.updateStatements;
                StatementsHashMap<TableModel, PreparedStatement> thisInsertStatements = this.insertStatements;) {
            super.close();
        }
    }
}