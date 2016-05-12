package pt.tecnico.myDrive.service.dto;

public class EnvVarDto {

	private String name;
	private String value;
	
	public EnvVarDto(String name, String value){
		this.name = name;
		this.value = value;
	}
	
	public final String getName(){
		return name;
	}
	
	public final String getValue(){
		return value;
	}
}
