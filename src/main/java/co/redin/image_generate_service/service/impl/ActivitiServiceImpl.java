package co.redin.image_generate_service.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.h2.tools.Server;
import org.springframework.stereotype.Component;

import co.redin.image_generate_service.service.ActivitiService;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ActivitiServiceImpl implements ActivitiService {

	private ProcessEngine engine;
	private Server h2Server;	

	@Override
	public void startup() throws Exception {
		// init h2 memory db
		this.h2Server = Server.createTcpServer("-tcpPort", "9123", "-tcpPassword", "our_secrets");
		this.h2Server.start();

		StandaloneProcessEngineConfiguration conf = new StandaloneProcessEngineConfiguration();
		conf.setJdbcDriver("org.h2.Driver");
		conf.setJdbcUrl("jdbc:h2:tcp://localhost:9123/mem:activiti;DB_CLOSE_DELAY=1000");
		conf.setJdbcUsername("sa");
		conf.setJdbcPassword("our_secrets");
		conf.setDatabaseSchemaUpdate("true");
		conf.setAsyncExecutorActivate(true);
		this.engine = conf.buildProcessEngine();
		log.info("Activiti Service Startup.");
	}

	@Override
	public void shutdown() throws Exception {
		this.engine.close();
		this.h2Server.stop();
		log.info("Activiti Service Shutdown.");
	}

	@Override
	public void generateActivitiImage(String xml, String path) throws Exception {
		RepositoryService repositoryService = this.engine.getRepositoryService();

		Deployment deploy = repositoryService.createDeployment().addString("foo.bpmn", xml).deploy();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.deploymentId(deploy.getId()).singleResult();
		BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
		InputStream imageStream = new DefaultProcessDiagramGenerator().generatePngDiagram(bpmnModel);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int reads = imageStream.read();

		while (reads != -1) {
			baos.write(reads);
			reads = imageStream.read();
		}
		imageStream.close();
		repositoryService.deleteDeployment(deploy.getId(), true);

		FileOutputStream outputStream = new FileOutputStream(path);
		outputStream.write(baos.toByteArray());
		outputStream.close();

	}

}
