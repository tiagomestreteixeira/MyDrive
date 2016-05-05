package pt.tecnico.myDrive.service.dto;

import org.joda.time.DateTime;

public class FileDto {
	private int id;
	private String filename;
	private DateTime lastModification;
	private String permissions;
	private String type;
	private String content;

	public FileDto(int id, String filename, DateTime lastModification, String permissions, String type, String content) {
		this.id = id;
		this.filename = filename;
		this.lastModification = lastModification;
		this.permissions = permissions;
		this.type = type;
		this.content = content;
	}

	public FileDto(int id, String filename, DateTime lastModification, String permissions, String type) {
		this.id = id;
		this.filename = filename;
		this.lastModification = lastModification;
		this.permissions = permissions;
		this.type = type;
		this.content = "";
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
}
