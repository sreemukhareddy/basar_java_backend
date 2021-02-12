package com.hoaxify;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer{
	
	@Autowired
	private AppConfiguration appConfig;
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/images/**")
				.addResourceLocations("file:" + appConfig.getUploadpath() + "/")
				.setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));
	}

	@Bean
	public CommandLineRunner getCommandLineRunner() {
		return args -> {
			settingUpFilePaths(appConfig.getUploadpath());
			settingUpFilePaths(appConfig.getProfileImagesPath());
			settingUpFilePaths(appConfig.getFullAttachmentsPath());
		};
	}

	private void settingUpFilePaths(String path) {
		File uploadFolder = new File(path);
		boolean uploadFolderExist = uploadFolder.exists() && uploadFolder.isDirectory();
		if(!uploadFolderExist) {
			uploadFolder.mkdir();
		}
	}
}
