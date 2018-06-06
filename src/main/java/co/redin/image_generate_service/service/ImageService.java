package co.redin.image_generate_service.service;

public interface ImageService {

	byte[] getHashPng(String hash) throws Exception;

	String getImageHashFromText(String text) throws Exception;
}
