package pt.tecnico.myDrive.service.dto;

import org.joda.time.DateTime;

public class FileDto {
	private int id;
	private String filename;
	private DateTime lastModification;
	private String permissions;
	private String type;
	private String content;
	private String owner;

	public FileDto(int id, String filename, DateTime lastModification, String permissions, String type, String content, String owner) {
		this.id = id;
		this.filename = filename;
		this.lastModification = lastModification;
		this.permissions = permissions;
		this.type = type;
		this.content = content;
		this.owner = owner;
	}

	public FileDto(int id, String filename, DateTime lastModification, String permissions, String type, String owner) {
		this.id = id;
		this.filename = filename;
		this.lastModification = lastModification;
		this.permissions = permissions;
		this.type = type;
		this.content = "";
		this.owner = owner;
	}

	public final int getId() {
		return id;
	}

	public final String getFilename() {
		return filename;
	}

	public final DateTime getLastModification() {
		return lastModification;
	}

	public final String getPermissions() {
		return permissions;
	}

	public final String getContent() {
		return content;
	}

	public final String getType() {
		return type;
	}
	
	public final String getOwner(){
		return owner;
	}
}
