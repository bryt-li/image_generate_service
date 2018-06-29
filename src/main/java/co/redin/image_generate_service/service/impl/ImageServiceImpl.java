package co.redin.image_generate_service.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.redin.image_generate_service.service.ActivitiService;
import co.redin.image_generate_service.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

@Component
@Slf4j
public class ImageServiceImpl implements ImageService {

	@Autowired
	private ActivitiService activitiService;
	
	private final String HASH_FILE_DIR = "/tmp/image_server";

	public ImageServiceImpl() {
	}

	@Override
	public byte[] getHashPng(String hash) throws Exception {
		String path = HASH_FILE_DIR + File.pathSeparator + hash + ".png";
		FileInputStream fis = new FileInputStream(path);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int reads = fis.read();

		while (reads != -1) {
			baos.write(reads);
			reads = fis.read();
		}
		fis.close();
		return baos.toByteArray();
	}

	@Override
	public String getImageHashFromText(String text) throws Exception {
		text = Strings.trimToNull(text);
		
		String hash = getHash(text);
		String path = HASH_FILE_DIR + File.pathSeparator + hash + ".png";

		Path f = Paths.get(path);
		Path d = Paths.get(HASH_FILE_DIR);

		if (!Files.isDirectory(d)) {
			new File(HASH_FILE_DIR).mkdirs();
		}

		if (!Files.exists(f)) {
			if (text.trim().startsWith("@start")) {
				generatePlantumlImage(text, path);
			} else if (text.trim().startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")) {
				activitiService.generateActivitiImage(text, path);
			}else {
				log.debug(text);
				throw new Exception("invalid text format.");
			}
		}
		return hash;
	}
	
	private void generatePlantumlImage(String uml, String filePath) throws Exception {
		FileOutputStream outputStream = new FileOutputStream(filePath);
		SourceStringReader reader = new SourceStringReader(uml);
		reader.outputImage(outputStream, new FileFormatOption(FileFormat.PNG, false));
		outputStream.close();
	}

	private String getHash(String text) throws Exception {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(StandardCharsets.UTF_8.encode(text));
		String hash = String.format("%032x", new BigInteger(1, md5.digest()));
		return hash;
	}
}
