package com.hoaxify.hoax.vm;

import com.hoaxify.filecontroller.FileAttachmentVM;
import com.hoaxify.hoax.Hoax;
import com.hoaxify.user.vm.UserVM;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HoaxVM {

	private long id;
	private String content;
	private long date;
	private UserVM user;
	private FileAttachmentVM attachment;
	
	public HoaxVM(Hoax hoax) {
		this.id = hoax.getId();
		this.content = hoax.getContent();
		this.date = hoax.getTimestamp().getTime();
		this.user = new UserVM(hoax.getUser());
		if(hoax.getAttachment() != null) {
			this.attachment = new FileAttachmentVM(hoax.getAttachment());
		}
	}
	
}
