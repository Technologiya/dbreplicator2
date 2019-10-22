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

/**
 * Функции для работы с метаданными
 * 
 */
package ru.taximaxim.dbreplicator2.replica.strategies.replication.data;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

import ru.taximaxim.dbreplicator2.el.FatalReplicationException;

/**
 * Заготовка класса для работы с реплицируемыми данными
 * 
 * @author volodin_aa
 *
 */
public class DataServiceSkeleton implements AutoCloseable {

    private final DataSource dataSource;
    private Connection connection;

    /**
     * Конструктор на основе подключения к БД
     * 
     * @param connection
     *            подключение к БД
     */
    public DataServiceSkeleton(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Функция получения и настройки подключения к БД По умолчанию настраивается
     * AutoCommit и TRANSACTION_READ_COMMITTED
     * 
     * @return the connection
     * @throws SQLException
     */
    public Connection getConnection() throws FatalReplicationException {
        if (connection == null) {
            try {
                connection = dataSource.getConnection();
                // Настраиваем соединение
                connection.setAutoCommit(true);
                connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            } catch (SQLException e) {
                throw new FatalReplicationException("Ошибка создания соединения", e);
            }
        }
        return connection;
    }

    @Override
    public void close() throws FatalReplicationException {
        try {
        if (connection != null && !connection.isClosed()) {
                connection.close();
        }
        } catch (SQLException e) {
            throw new FatalReplicationException("Ошибка закрытия соединения", e);
        }
    }
    
    /**
     * @return the connectionFactory
     */
    protected DataSource getDataSource() {
        return dataSource;
    }

}
