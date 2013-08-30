package org.virtualrepository.service.utils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class UndeployListener implements ServletContextListener {

	
	public void contextInitialized(ServletContextEvent sce) {};
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		CdiProducers.repository().shutdown();
		
	}
}
