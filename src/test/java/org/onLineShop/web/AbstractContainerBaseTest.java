package org.onLineShop.web;

import org.testcontainers.containers.MySQLContainer;

        
public class AbstractContainerBaseTest {
	
	/*private static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:5.7");
	@DynamicPropertySource
	   public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
	      registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
	      // take these values dynamically also
	       registry.add("spring.datasource.password", mySQLContainer::getPassword);
	       registry.add("spring.datasource.username", mySQLContainer::getUsername);}*/
	static final MySQLContainer<?> MY_SQL_CONTAINER;

    static {
    	//mySQLContainer.start();
       MY_SQL_CONTAINER = new MySQLContainer<>("mysql:lastest")
    		             .withDatabaseName("test")
    		             .withUsername("test")
    		             .withPassword("test");
       MY_SQL_CONTAINER.start();
    }
}
