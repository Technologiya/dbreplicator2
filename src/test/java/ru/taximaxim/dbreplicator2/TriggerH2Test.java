package ru.taximaxim.dbreplicator2;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ru.taximaxim.dbreplicator2.cf.ConnectionFactory;

public class TriggerH2Test {
    
    protected static final Logger LOG = Logger.getLogger(H2ManagerTest.class);
    protected static SessionFactory sessionFactory;
    protected static Session session;
    protected static ConnectionFactory connectionFactory;
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        sessionFactory = Application.getSessionFactory();
        session = sessionFactory.openSession();
        connectionFactory = Application.getConnectionFactory();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
          connectionFactory.close();
          session.close();
          sessionFactory.close();
    }

    @Test
    public void testTrigger() throws SQLException, ClassNotFoundException, IOException {
        
        String source = "source";
        Connection conn = connectionFactory.getConnection(source);
        
        Helper.executeSqlFromFile(conn, "importRep2.sql");
        Helper.executeSqlFromFile(conn, "importSource.sql");
        Helper.createTrigger(conn, "t_table");
        Helper.executeSqlFromFile(conn, "importSourceData.sql");
        
        int countT_TABLE = Helper.InfoCount(conn, "t_table");
        if(countT_TABLE==0) {
            LOG.error("Таблице t_table не должна пустой: count = " + countT_TABLE);
        }
        Assert.assertNotEquals(countT_TABLE, 0);
        
        int countrep2_superlog = Helper.InfoCount(conn, "rep2_superlog");
        if(countrep2_superlog==0) {
            LOG.error("Таблица rep2_superlog не должна пустой: count = " + countrep2_superlog);
        }
        Assert.assertNotEquals(countrep2_superlog, 0);
        
        LOG.info("<====== t_table ======>");
        Helper.InfoSelect(conn, "t_table");
        LOG.info(">====== t_table ======<");
        
        LOG.info("<====== rep2_superlog ======>");
        Helper.InfoSelect(conn, "rep2_superlog");
        LOG.info("======= rep2_superlog =======");
        conn.close();
    }
}
