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

package ru.taximaxim.dbreplicator2.hibernate;

import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import ru.taximaxim.dbreplicator2.ConnectionsFactory.BoneCPDataBaseSettingsStorage;
import ru.taximaxim.dbreplicator2.ConnectionsFactory.BoneCPSettings;

/**
 * Хранилище настроек именнованных соединений к BoneCP на основе Hibernate
 * 
 * @author volodin_aa
 *
 */
public class HibernateBoneCPSettingsStorage implements BoneCPDataBaseSettingsStorage {
    
    /**
     * Хранилище настроек
     */
    protected SessionFactory sessionFactory;
    
    /**
     * Конструктор хранилища настроек в Hibernate
     * 
     * @param sessionFactory - фабрика сессий Hibernate
     */
    public HibernateBoneCPSettingsStorage(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public BoneCPSettings getDataBaseSettingsByName(String poolName) {
        Session session = sessionFactory.openSession();
        try {
            return (BoneCPSettings) session.get(PersistenceBoneCPSettings.class, poolName);
        } finally {
            session.close();
        }
    }

    @Override
    public Map<String, BoneCPSettings> getDataBaseSettings() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setDataBaseSettings(BoneCPSettings dataBaseSettings) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            session.save(dataBaseSettings);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public void delDataBaseSettings(BoneCPSettings dateBaseSettings) {
        // TODO Auto-generated method stub        
    }

}
