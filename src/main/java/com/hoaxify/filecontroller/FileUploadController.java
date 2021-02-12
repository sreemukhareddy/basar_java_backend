package com.hoaxify.filecontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hoaxify.userservice.FileService;

@RestController
@RequestMapping("/api/1.0")
public class FileUploadController {
	
	@Autowired
	private FileService fileService;

	@PostMapping("/hoaxes/upload")
	public FileAttachment uploadforHoax(MultipartFile file) {
		return fileService.saveAttachment(file);
	}
}
