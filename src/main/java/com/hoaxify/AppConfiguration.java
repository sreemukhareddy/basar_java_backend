package com.hoaxify;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "hoaxify")
public class AppConfiguration {

	private String uploadpath;

	private String profileImagesPath = "profile";

	public String getProfileImagesPath() {
		return this.uploadpath + "/" + this.profileImagesPath;
	}

	private String fullAttachmentsPath = "attachments";

	public String getFullAttachmentsPath() {
		return this.uploadpath + "/" + this.fullAttachmentsPath;
	}
}
