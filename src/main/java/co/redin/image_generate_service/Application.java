package co.redin.image_generate_service;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import co.redin.image_generate_service.service.ActivitiService;
import lombok.extern.java.Log;

@SpringBootApplication
@Component
@Log
public class Application {

	@Autowired
	private ActivitiService activitiService;
	
    private static ConfigurableApplicationContext context;

    public void startup() throws Exception {
    	this.activitiService.startup();
        System.out.println("application startup.");
        log.info("application shartup.");
    }

    public void shutdown() throws Exception{
    	this.activitiService.shutdown();
        // for some reason, the log is unavailable when shutdown
        System.out.println("application shutdown.");
        log.info("application shutdown.");
    }

    public static void main(String[] args) {
        context = SpringApplication.run(Application.class, args);
        Application application = context.getBean(Application.class);
        try {
            application.startup();
        } catch (Exception e) {
            e.printStackTrace();
            log.severe(String.format("Application startup failed. exception=[%s]", e.getMessage()));
            context.close();
        }
    }

    @PreDestroy
    public void destroy() {
        try {
            this.shutdown();
        } catch (Exception e) {
            log.warning(String.format("application shutdown failed. exception=[%s])", e.getMessage()));
            // for some reason, the log is unavailable when shutdown
            e.printStackTrace();
        }
    }
}
