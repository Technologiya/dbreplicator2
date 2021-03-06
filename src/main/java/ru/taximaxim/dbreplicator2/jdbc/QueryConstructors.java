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
 * Функции для конструирования запросов
 * 
 */
package ru.taximaxim.dbreplicator2.jdbc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author volodin_aa
 * 
 */
public final class QueryConstructors {

    private static final String EQUAL = " = ? ";
    private static final String QUESTION = "?";
    private static final String DELIMITER = ", ";
    private static final String INSERT_INTO = "INSERT INTO ";
    private static final String VALUES = ") VALUES (";
    private static final String SELECT = "SELECT ";
    private static final String FROM = " FROM ";
    private static final String WHERE = " WHERE ";
    private static final String ORDER_BY = " ORDER BY ";
    private static final String AND = " AND ";
    private static final String OR = " OR ";
    private static final String DELETE_FROM = "DELETE FROM ";
    private static final String UPDATE = "UPDATE ";
    private static final String SET = " SET ";

    /**
     * Сиглетон
     */
    private QueryConstructors() {
    }

    /**
     * Добавляет postfix к каждой колонке, если колонка кастуется, добавляем
     * выражение кастования
     * 
     * @param list
     * @param castCols
     * @param postfix
     * @return
     */
    public static Collection<String> listAddPostfix(Collection<?> list,
            Map<String, String> castCols, String postfix) {

        Collection<String> newList = new ArrayList<>();
        for (Object val : list) {
            if (castCols.keySet().contains(val)) {
                newList.add(val + "=" + castCols.get(val));
            } else {
                newList.add(val + postfix);
            }
        }
        return newList;
    }

    /**
     * Заменяет в списке колонку на кастуемое выражение
     * 
     * @param list
     * @param castCols
     * @return
     */
    public static Collection<String> listToCastString(Collection<?> list,
            Map<String, String> castCols) {
        Collection<String> newList = new ArrayList<>();
        for (Object val : list) {
            if (castCols.keySet().contains(val)) {
                newList.add(castCols.get(val));
            } else {
                newList.add(val.toString());
            }
        }
        return newList;
    }

    /**
     * Строит строку из элементов списка, разделенных разделителем delimiter
     * 
     * @param list
     *            список объектов
     * @param delimiter
     *            разделитель
     * @return строка из элементов списка, разделенных разделителем delimiter
     */
    public static String listToString(Collection<?> list, String delimiter) {
        StringBuilder result = new StringBuilder();
        boolean setComma = false;
        for (Object val : list) {
            if (setComma) {
                result.append(delimiter);
            } else {
                setComma = true;
            }
            result.append(val);
        }
        return result.toString();
    }

    /**
     * Строит строку из элементов списка с добавленным postfix в конце и
     * разделенных разделителем delimiter
     * 
     * @param list
     *            список объектов
     * @param delimiter
     *            разделитель
     * @param postfix
     *            строка для добавления после строки элемента
     * @return строка из элементов списка с добавленным postfix в конце и
     *         разделенных разделителем delimiter
     */
    public static String listToString(Collection<?> list, String delimiter,
            String postfix) {
        return listToString(listAddPostfix(list, Collections.emptyMap(), postfix),
                delimiter);
    }

    /**
     * Строит строку из элементов списка, разделенных разделителем delimiter с
     * заменой значений колонок выражениями из карты подстановки
     * 
     * @param list
     *            список объектов
     * @param castCols
     *            карта подстановки вместо колонок
     * @param delimiter
     *            разделитель
     * @return строка из элементов списка, разделенных разделителем delimiter
     */
    public static String listToString(Collection<?> list, Map<String, String> castCols,
            String delimiter) {
        return listToString(listToCastString(list, castCols), delimiter);
    }

    /**
     * Строит строку из элементов списка с добавленным postfix в конце и
     * разделенных разделителем delimiter с кастованием полей
     * 
     * @param list
     *            список объектов
     * @param delimiter
     *            разделитель
     * @param postfix
     *            строка для добавления после строки элемента
     * @return строка из элементов списка с добавленным postfix в конце и
     *         разделенных разделителем delimiter
     */
    public static String listToString(Collection<?> list, Map<String, String> castCols,
            String delimiter, String postfix) {

        return listToString(listAddPostfix(list, castCols, postfix), delimiter);
    }

    /**
     * Строит список вопросов для передачи экранированых параметров
     * 
     * @param colsList
     *            список колонок
     * @return список вопросов для передачи экранированых параметров
     */
    public static Collection<String> questionMarks(Collection<?> colsList) {
        return questionMarks(colsList, Collections.emptyMap());
    }

    /**
     * Строит список вопросов для передачи экранированых параметров с заменой
     * значений колонок выражениями из карты подстановки
     * 
     * @param colsList
     *            список колонок
     * @param castCols
     *            карта подстановки вместо колонок
     * @return список вопросов для передачи экранированых параметров
     */
    public static Collection<String> questionMarks(Collection<?> colsList,
            Map<String, String> castCols) {
        Collection<String> result = new ArrayList<>();
        for (Object col : colsList) {
            if (castCols.keySet().contains(col)) {
                result.add(castCols.get(col));
            } else {
                result.add(QUESTION);
            }
        }
        return result;
    }

    /**
     * Генерирует строку запроса для вставки данных
     * 
     * @param tableName
     *            имя целевой таблицы
     * @param colsList
     *            список колонок
     * @return строка запроса для вставки данных
     */
    public static String constructInsertQuery(String tableName,
            Collection<String> colsList) {
        return constructInsertQuery(tableName, colsList, Collections.emptyMap());
    }

    /**
     * Генерирует строку запроса для вставки данных с подменой значений колонок
     * выражениями из карты подстановки
     * 
     * @param tableName
     *            имя целевой таблицы
     * @param colsList
     *            список колонок
     * @param castCols
     *            карта подстановки вместо колонок
     * @return строка запроса для вставки данных
     */
    public static String constructInsertQuery(String tableName,
            Collection<String> colsList, Map<String, String> castCols) {
        StringBuilder insertQuery = new StringBuilder().append(INSERT_INTO)
                .append(tableName).append('(').append(listToString(colsList, DELIMITER))
                .append(VALUES)
                .append(listToString(questionMarks(colsList, castCols), DELIMITER))
                .append(')');

        return insertQuery.toString();
    }

    /**
     * Генерирует строку запроса следующего вида: INSERT INTO
     * <table>
     * (<cols>) SELECT (<questionsMarks>) Это позволяет создавать запросы на
     * вставку по условию
     * 
     * @param tableName
     *            имя целевой таблицы
     * @param colsList
     *            список колонок
     * @return строка запроса вставки из запроса выборки
     */
    public static String constructInsertSelectQuery(String tableName,
            Collection<String> colsList) {
        return constructInsertSelectQuery(tableName, colsList,
                Collections.emptyMap());
    }

    /**
     * Генерирует строку запроса следующего вида: INSERT INTO
     * <table>
     * (<cols>) SELECT (<questionsMarks>) Это позволяет создавать запросы на
     * вставку по условию с поддержкой кастования полей
     * 
     * @param tableName
     *            имя целевой таблицы
     * @param colsList
     *            список колонок
     * @return строка запроса вставки из запроса выборки
     */
    public static String constructInsertSelectQuery(String tableName,
            Collection<String> colsList, Map<String, String> castCols) {

        StringBuilder insertQuery = new StringBuilder().append(INSERT_INTO)
                .append(tableName).append('(').append(listToString(colsList, DELIMITER))
                .append(") ")
                .append(constructSelectQuery(questionMarks(colsList, castCols)));

        return insertQuery.toString();
    }

    /**
     * Создает строку запроса на выборку данных
     * 
     * @param colsList
     *            список колонок для вставки
     * @return строкf запроса на выборку данных
     */
    public static String constructSelectQuery(Collection<String> colsList) {
        return constructSelectQuery(colsList, Collections.emptyMap());
    }

    /**
     * Создает строку запроса на выборку данных с кастование полей
     * 
     * @param colsList
     *            список колонок для вставки
     * @return строкf запроса на выборку данных
     */
    public static String constructSelectQuery(Collection<String> colsList,
            Map<String, String> castCols) {
        StringBuilder query = new StringBuilder().append(SELECT)
                .append(listToString(colsList, castCols, DELIMITER));

        return query.toString();
    }

    /**
     * Создает строку запроса на выборку данных из таблицы
     * 
     * @param tableName
     *            имя целевой таблицы
     * @param colsList
     *            список колонок
     * @return строка запроса на выборку данных из таблицы
     */
    public static String constructSelectQuery(String tableName,
            Collection<String> colsList) {
        return constructSelectQuery(tableName, colsList, Collections.emptyMap());
    }

    /**
     * Создает строку запроса на выборку данных из таблицы с кастованием полей
     * 
     * @param tableName
     *            имя целевой таблицы
     * @param colsList
     *            список колонок
     * @return строка запроса на выборку данных из таблицы
     */
    public static String constructSelectQuery(String tableName,
            Collection<String> colsList, Map<String, String> castCols) {
        StringBuilder query = new StringBuilder(constructSelectQuery(colsList, castCols))
                .append(FROM).append(tableName);

        return query.toString();
    }

    /**
     * Создает строку запроса на выборку данных из таблицы с условием
     * 
     * @param tableName
     *            имя целевой таблицы
     * @param colsList
     *            список колонок
     * @param whereList
     *            список колонок условия
     * @return строка запроса на выборку данных из таблицы с условием
     */
    public static String constructSelectQuery(String tableName,
            Collection<String> colsList, Collection<String> whereList, String where) {
        return constructSelectQuery(tableName, colsList, Collections.emptyMap(),
                whereList, where);
    }

    /**
     * Создает строку запроса на выборку данных из таблицы с условием с
     * кастованием полей
     * 
     * @param tableName
     *            имя целевой таблицы
     * @param colsList
     *            список колонок
     * @param whereList
     *            список колонок условия
     * @return строка запроса на выборку данных из таблицы с условием
     */
    public static String constructSelectQuery(String tableName,
            Collection<String> colsList, Map<String, String> castCols,
            Collection<String> whereList, String where) {
        StringBuilder query = new StringBuilder(
                constructSelectQuery(tableName, colsList, castCols)).append(WHERE)
                        .append(listToString(whereList, AND, EQUAL));
        appendClause(query, AND, where);

        return query.toString();
    }

    /**
     * Создает строку запроса на выборку данных из таблицы с условием в
     * группировкой по ключевым полям
     * 
     * @param tableName
     * @param colsList
     * @param whereList
     * @param orderByList
     * @return
     */
    public static String constructSelectQuery(String tableName,
            Collection<String> colsList, Collection<String> whereList,
            Collection<String> orderByList, String where) {

        return constructSelectQuery(tableName, colsList, Collections.emptyMap(),
                whereList, orderByList, where);
    }

    /**
     * Создает строку запроса на выборку данных из таблицы с условием в
     * группировкой по ключевым полям с кастованием полей
     * 
     * @param tableName
     * @param colsList
     * @param castCols
     * @param whereList
     * @param orderByList
     * @param where
     * @return
     */
    public static String constructSelectQuery(String tableName,
            Collection<String> colsList, Map<String, String> castCols,
            Collection<String> whereList, Collection<String> orderByList, String where) {

        StringBuilder query = new StringBuilder(
                constructSelectQuery(tableName, colsList, castCols, whereList, where));
        query.append(ORDER_BY).append(listToString(orderByList, DELIMITER));

        return query.toString();
    }

    /**
     * Генерирует строку запроса для обновления данных
     * 
     * @param tableName
     *            имя целевой таблицы
     * @param colsList
     *            список колонок
     * @param whereList
     *            список колонок условия
     * @return строка запроса для обновления данных
     */
    public static String constructUpdateQuery(String tableName,
            Collection<String> colsList, Collection<String> whereList) {

        return constructUpdateQuery(tableName, colsList, Collections.emptyMap(),
                whereList);
    }

    /**
     * Генерирует строку запроса для обновления данных с кастованием полей
     * 
     * @param tableName
     *            имя целевой таблицы
     * @param colsList
     *            список колонок
     * @param whereList
     *            список колонок условия
     * @return строка запроса для обновления данных
     */
    public static String constructUpdateQuery(String tableName,
            Collection<String> colsList, Map<String, String> castCols,
            Collection<String> whereList) {

        StringBuilder insertQuery = new StringBuilder().append(UPDATE).append(tableName)
                .append(SET).append(listToString(colsList, castCols, DELIMITER, EQUAL))
                .append(WHERE).append(listToString(whereList, AND, EQUAL));

        return insertQuery.toString();
    }

    /**
     * Создает строку запроса на выборку данных из таблицы с условием
     * 
     * @param tableName
     *            имя целевой таблицы
     * @param colsList
     *            список колонок
     * @param where
     *            условие
     * @return строка запроса на выборку данных из таблицы с условием
     */
    public static String constructSelectQuery(String tableName,
            Collection<String> colsList, String where) {
        StringBuilder query = new StringBuilder(constructSelectQuery(tableName, colsList))
                .append(WHERE).append(where);

        return query.toString();
    }

    /**
     * Создает строку запроса на удаление данных из таблицы с условием
     * 
     * @param tableName
     *            имя целевой таблицы
     * @param whereList
     *            список колонок условия
     * @return
     */
    public static String constructDeleteQuery(String tableName,
            Collection<String> whereList) {
        StringBuilder query = new StringBuilder().append(DELETE_FROM).append(tableName)
                .append(WHERE).append(listToString(whereList, AND, EQUAL));

        return query.toString();
    }

    /**
     * Добавление заданного выражения к запросу
     * 
     * @param query
     *            запрос
     * @param clause
     *            выражение
     * @param text
     *            содержимое выражения
     * 
     * @param query
     * @param text
     */
    public static void appendClause(StringBuilder query, String clause, String text) {
        if ((text != null) && (!text.isEmpty())) {
            query.append(clause).append(text);
        }
    }

    /**
     * Добавление заданного выражения к запросу
     * 
     * @param query
     *            запрос
     * @param clause
     *            выражение
     * @param value
     *            значение
     */
    public static void appendClause(StringBuilder query, String clause, Integer value) {
        if (value != null) {
            query.append(clause).append(value);
        }
    }

    /**
     * Генерация условия сдвига по составному ключу
     * 
     * @param primaryKeys
     * @param selectQuery
     */
    public static StringBuilder getKeyShift(Collection<String> primaryKeys,
            String orderMode) {
        StringBuilder selectQuery = new StringBuilder();
        List<String> tempKeys = new ArrayList<>(primaryKeys);
        String postfix = "DESC".equals(orderMode) ? " < ? " : " > ? ";
        while (!tempKeys.isEmpty()) {
            selectQuery.append('(');
            for (int i = 0; i < tempKeys.size(); i++) {
                if (i != tempKeys.size() - 1) {
                    selectQuery.append(tempKeys.get(i)).append(EQUAL).append(AND);
                } else {
                    selectQuery.append(tempKeys.get(i)).append(postfix);
                }
            }
            tempKeys.remove(tempKeys.size() - 1);
            selectQuery.append(')');
            if (!tempKeys.isEmpty()) {
                selectQuery.append(OR);
            }
        }
        return selectQuery;
    }

    /**
     * Список параметров для условия сдвига по составному ключу
     * 
     * @param primaryKeys
     * @param selectQuery
     */
    public static Collection<String> getKeyShiftParams(Collection<String> primaryKeys) {
        LinkedList<String> keys = new LinkedList<>(primaryKeys);
        Collection<String> cols = new LinkedList<>();
        while (!keys.isEmpty()) {
            cols.addAll(keys);
            keys.removeLast();
        }

        return cols;
    }

    /**
     * AND (where)
     * 
     * @param query
     * @param where
     */
    public static void appendWhereParam(StringBuilder query, String where) {
        if ((where != null) && (!where.isEmpty())) {
            query.append(AND).append('(').append(where).append(')');
        }
    }

    /**
     * WHERE (where)
     * 
     * @param query
     * @param where
     */
    public static void appendWhere(StringBuilder query, String where) {
        if ((where != null) && (!where.isEmpty())) {
            query.append(WHERE).append('(').append(where).append(')');
        }
    }
}
