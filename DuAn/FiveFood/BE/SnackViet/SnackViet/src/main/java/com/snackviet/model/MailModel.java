package com.snackviet.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.snackviet.model.MailModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MailModel {
	String form = "tranvn7849@gmail.com";
	String to;
	String subject;
	String content;
	List<String> cc = new ArrayList<String>();
	List<String> bcc = new ArrayList<String>();
	List<File> attachs = new ArrayList<File>();
	
	public MailModel(String to, String subject, String content) {
		super();
		this.to = to;
		this.subject = subject;
		this.content = content;
	}
	
}
