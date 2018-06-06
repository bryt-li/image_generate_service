package co.redin.image_generate_service.controller;

import java.net.URLDecoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import co.redin.image_generate_service.service.ImageService;

@RestController
public class ImageController extends BaseController{
	@Autowired
	private ImageService imageService;
	
	@PostMapping(value = "/text")
	public ResponseEntity<String> text(@RequestBody String text) throws Exception {
		String decoded = URLDecoder.decode(text, "UTF-8");
		String hash = imageService.getImageHashFromText(decoded);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        return new ResponseEntity<>(hash, headers, HttpStatus.OK);
	}
	
    @GetMapping(value = "/png/{hash}")
	public @ResponseBody byte[] png(@PathVariable("hash") String hash) throws Exception {
		return imageService.getHashPng(hash);
	}
}
