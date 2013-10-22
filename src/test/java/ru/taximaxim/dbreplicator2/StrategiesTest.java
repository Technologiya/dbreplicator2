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
package ru.taximaxim.dbreplicator2;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

import ru.taximaxim.dbreplicator2.model.RunnerModel;
import ru.taximaxim.dbreplicator2.model.StrategyModel;

public class StrategiesTest {

	protected static SessionFactory sessionFactory;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	    Configuration configuration = new Configuration();
	    configuration.configure();
	    
	    // http://stackoverflow.com/a/15702946/2743959
	    ServiceRegistry serviceRegistry = 
	    	    new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();        
	    sessionFactory = configuration.buildSessionFactory(serviceRegistry);		
	}

	@Test
	public void test() {
		
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        RunnerModel runner = createRunner("source", "target", "description");
        StrategyModel strategy = createStrategy("ru.taximaxim.Class", null, true, 100);
        
        addStrategy(runner, strategy);
        
        session.saveOrUpdate(runner);
	}
	
	public RunnerModel createRunner(String source, String target, String description) {

		RunnerModel runner = new RunnerModel();
		
		runner.setSource(source);
		runner.setTarget(target);
		runner.setDescription(description);
		
		return runner;
	}
	
	public StrategyModel createStrategy(String className, String param, 
			boolean isEnabled, int priority) {
		
		StrategyModel strategy = new StrategyModel();
		
		strategy.setClassName(className);
		strategy.setParam(param);
		strategy.setEnabled(isEnabled);
		strategy.setPriority(priority);
		
		return strategy;
	}
	
	public void addStrategy(RunnerModel runner, StrategyModel strategy) {
		
		runner.getStrategyModels().add(strategy);
		strategy.setRunner(runner);
		
	}
}

