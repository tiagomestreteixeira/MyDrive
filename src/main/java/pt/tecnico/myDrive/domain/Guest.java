package pt.tecnico.myDrive.domain;

public class Guest extends Guest_Base {

	public Guest() {
		super();
	}

	@Override
	public boolean checkPassword(String attempt) {
		return true;
	}
}
