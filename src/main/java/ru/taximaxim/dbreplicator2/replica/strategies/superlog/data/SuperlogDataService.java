/*
// * The MIT License (MIT)
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

package ru.taximaxim.dbreplicator2.replica.strategies.superlog.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Интерфейс для реализации дата сервисов обработки суперлога
 * @author petrov_im
 *
 */
public interface SuperlogDataService extends AutoCloseable {
    
    /**
     * Получение PreparedStatement для первоначальной выборки данных из суперлога
     * @return
     * @throws SQLException
     */
    PreparedStatement getInitSelectSuperlogStatement() throws SQLException;
    
    /**
     * Получение PreparedStatement для последующих выборок данных из суперлога
     * @return
     */
    PreparedStatement getSelectSuperlogStatement() throws SQLException;
    
    /**
     * Получение PreparedStatement для удаления записей из суперлога
     * @return
     */
    PreparedStatement getDeleteSuperlogStatement() throws SQLException;
    
    /**
     * Получение PreparedStatement для вставки данных в воркпул 
     * @return
     */
    PreparedStatement getInsertWorkpoolStatement() throws SQLException;

    /**
     * Получение коннекшена выборки из суперлога
     * @return
     * @throws SQLException 
     */
    Connection getSelectConnection() throws SQLException;

    /**
     * Получение коннекшена удаления из суперлога
     * @return
     * @throws SQLException 
     */
    Connection getDeleteConnection() throws SQLException;

    /**
     * Получение коннекшена вставки данных в воркпул 
     * @return
     * @throws SQLException 
     */
    Connection getTargetConnection() throws SQLException;
}
