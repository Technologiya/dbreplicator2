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

package ru.taximaxim.dbreplicator2.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;


/**
 * Персистентный класс настроек HikariCP
 *
 * @author petrov_im
 *
 */
@Entity
@Table(name = "hikari_cp_settings")
public class HikariCPSettingsModel implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private static final int MAXIMUM_POOL_SIZE = 3;
    private static final boolean INITIALIZATION_FAIL_FAST = false;
    private static final int CONNECTION_TIMEOUT = 30000;
    private static final int IDLE_TIMEOUT = 600000;
    private static final int MAX_LIFETIME = 600000;

    /**
     * Конструктор по умолчанию
     */
    public HikariCPSettingsModel() {}

    /**
     * Имя пула
     */
    private String poolId;
    /**
     * Наименование драйвера БД
     */
    private String driver;
    /**
     * Строка подключения к БД
     */
    private String url;
    /**
     * Имя пользователя
     */
    private String user;
    /**
     * Пароль
     */
    private String pass;
    
    /**
     * Таймаут получения соединения (по-умолчанию 30 секунд)
     */
    private int connectionTimeout;
    
    /**
     * Время, в течении которого соединение может находиться в
     * соостоянии idle (по-умолчанию 10 минут)
     */
    private int idleTimeout;
    
    /**
     * Максимальное время жихни соедения в пуле
     * (по-умолчанию 30 минут)
     */
    private int maxLifetime;
  
    /**
     * Максимальное количество соединений
     */
    private int maximumPoolSize;

    private boolean initializationFailFast;

    public HikariCPSettingsModel(String poolId, String driver, String url, String user,
            String pass, int maximumPoolSize, boolean initializationFailFast, 
            int connectionTimeout, int idleTimeout, int maxLifetime) {
        this.poolId = poolId;
        this.driver = driver;
        this.url = url;
        this.user = user;
        this.pass = pass;
        this.maximumPoolSize = maximumPoolSize;
        this.initializationFailFast = initializationFailFast;
        this.connectionTimeout = connectionTimeout;
        this.idleTimeout = idleTimeout;
        this.maxLifetime = maxLifetime;
    }
    
    public HikariCPSettingsModel(String poolId, String driver, String url, String user,
            String pass) {
        this.poolId = poolId;
        this.driver = driver;
        this.url = url;
        this.user = user;
        this.pass = pass;
        this.maximumPoolSize = MAXIMUM_POOL_SIZE;
        this.initializationFailFast = INITIALIZATION_FAIL_FAST;
        this.connectionTimeout = CONNECTION_TIMEOUT;
        this.idleTimeout = IDLE_TIMEOUT;
        this.maxLifetime = MAX_LIFETIME;
    }

    /**
     * Получение имени пула соединений
     * 
     * @return the name
     */
    @Id
    @Column(name = "id_pool")
    public String getPoolId() {
        return poolId;
    }

    /**
     * Установка имени пула
     * 
     * @param name
     *            the name to set
     */
    public void setPoolId(String poolId) {
        this.poolId = poolId;
    }

    /**
     * Получение дравера БД 
     * 
     * @return the driver
     */
    @Column(name = "driver")
    public String getDriver() {
        return driver;
    }

    /**
     * Измение драйвера
     * 
     * @param driver
     *            the driver to set
     */
    public void setDriver(String driver) {
        this.driver = driver;
    }

    /**
     * @return the url
     */
    @Column(name = "url")
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     *            the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the user
     */
    @Column(name = "user")
    public String getUser() {
        return user;
    }

    /**
     * @param user
     *            the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the pass
     */
    @Column(name = "pass")
    public String getPass() {
        return pass;
    }

    /**
     * @param pass
     *            the pass to set
     */
    public void setPass(String pass) {
        this.pass = pass;
    }

    /**
     * @return the maximumPoolSize
     */
    @Column(name = "max_pool_size")
    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    /**
     * @param maximumPoolSize
     *            the maximumPoolSize to set
     */
    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    /**
     * @return the initializationFailFast
     */
    @Column(name = "init_fail_fast")
    public boolean getInitializationFailFast() {
        return initializationFailFast;
    }

    /**
     * @param initializationFailFast
     *            the initializationFailFast to set
     */
    public void setInitializationFailFast(boolean initializationFailFast) {
        this.initializationFailFast = initializationFailFast;
    }

    /**
     * @return the connectionTimeout
     */
    @Column(name = "connection_timeout")
    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    /**
     * @param connectionTimeout
     *            the connectionTimeout to set
     */
    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    /**
     * @return the idleTimeout
     */
    @Column(name = "idle_timeout")
    public int getIdleTimeout() {
        return idleTimeout;
    }

    /**
     * @param idleTimeout
     *            the idleTimeout to set
     */
    public void setIdleTimeout(int idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    /**
     * @return the maxLifetime
     */
    @Column(name = "max_lifetime")
    public int getMaxLifetime() {
        return maxLifetime;
    }

    /**
     * @param maxLifetime
     *            the maxLifetime to set
     */
    public void setMaxLifetime(int maxLifetime) {
        this.maxLifetime = maxLifetime;
    }

    /**
     * Список обработчиков
     */
    private List<RunnerModel> runners;

    /**
     * Получение списка раннеров 
     */
    @Column
    @OneToMany(targetEntity=RunnerModel.class, mappedBy="source", fetch=FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    public List<RunnerModel> getRunners() {
        if (runners == null) {
            runners = new ArrayList<RunnerModel>();
        }

        return runners;
    }
    
    
    public RunnerModel getRunner(int runnerId) {
        for (RunnerModel runner : getRunners()) {
            if (runner.getId() == runnerId) {
                return runner;
            }
        }
        return null;
    }
    
    /**
     * Сохранение списка обработчиков
     */
    public void setRunners(List<RunnerModel> runners) {
        this.runners = runners;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + connectionTimeout;
        result = prime * result + idleTimeout;
        result = prime * result + maxLifetime;
        result = prime * result + ((driver == null) ? 0 : driver.hashCode());
        result = prime * result + maximumPoolSize;
        result = prime * result + ((pass == null) ? 0 : pass.hashCode());
        result = prime * result + ((poolId == null) ? 0 : poolId.hashCode());
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof HikariCPSettingsModel)) {
            return false;
        }
        HikariCPSettingsModel other = (HikariCPSettingsModel) obj;
        if (poolId == null) {
            if (other.poolId != null) {
                return false;
            }
        } else if (!poolId.equals(other.poolId)) {
            return false;
        }
        if (url == null) {
            if (other.url != null) {
                return false;
            }
        } else if (!url.equals(other.url)) {
            return false;
        }
        return true;
    }
}