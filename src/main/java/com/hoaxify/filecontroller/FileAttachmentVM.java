package com.hoaxify.filecontroller;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FileAttachmentVM {
	
	private String name;
	
	private String fileType;

	public FileAttachmentVM(FileAttachment attachment) {
		this.name = attachment.getName();
		this.fileType = attachment.getFileType();
	}

}
