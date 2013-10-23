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

package ru.taximaxim.dbreplicator2.replica;

import java.sql.Connection;

import ru.taximaxim.dbreplicator2.model.StrategyModel;

/**
 * Базовый интерфейс-предок всех стратегий.
 * 
 * @author ags
 */
public interface Strategy {

	/**
	 * Отрабатывает стратегию кода. 
	 * 
	 * @param sourceConnection Источник БД
	 * @param targetConnection Целевая БД
	 * @param data Модель данных описывающая стратегию.
	 * 
	 * TODO: Может быть передавать не StrategyModel как избыточную, а лишь data?  
	 */
	public void execute(Connection sourceConnection, Connection targetConnection, 
			StrategyModel data) throws StrategyException;
	
}