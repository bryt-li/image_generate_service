package co.redin.image_generate_service.service;

public interface ActivitiService {

	void startup() throws Exception;
	void shutdown() throws Exception;
	void generateActivitiImage(String xml, String path) throws Exception;

}
