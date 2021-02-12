package com.hoaxify.userservice;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hoaxify.AppConfiguration;
import com.hoaxify.filecontroller.FileAttachment;
import com.hoaxify.filecontroller.FileAttachmentRepository;

@Service
public class FileService {

	@Autowired
	private AppConfiguration appConfiguration;
	
	@Autowired
	private FileAttachmentRepository fileAttachmentRepository;

	public String saveProfileImage(String base64Image) throws Exception {
		String imageName = UUID.randomUUID().toString().replaceAll("-", "");

		byte[] decodecBytres = Base64.getDecoder().decode(base64Image);

		File target = new File(appConfiguration.getProfileImagesPath() + "/" + imageName);
		FileUtils.writeByteArrayToFile(target, decodecBytres);
		return imageName;
	}

	public String detectType(byte[] fileArr) {
		Tika tika = new Tika();
		return tika.detect(fileArr);
	}

	public void deleteProfileImage(String image) {
		try {
			Files.deleteIfExists(Paths.get(appConfiguration.getProfileImagesPath()+"/"+image));
		} catch(Exception e) {
			
		}
	}

	public FileAttachment saveAttachment(MultipartFile file) {
		FileAttachment attachment = new FileAttachment();
		attachment.setDate(new Date());
		String randomName = UUID.randomUUID().toString().replace("-", "");
		attachment.setName(randomName);
		
		File target = new File(appConfiguration.getFullAttachmentsPath() + "/" + randomName);
		
		try {
			byte[] bytes = file.getBytes();
			FileUtils.writeByteArrayToFile(target, bytes);
			attachment.setFileType(detectType(bytes));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return fileAttachmentRepository.save(attachment);
	}
}
