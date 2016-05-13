package pt.tecnico.myDrive.presentation;

public class Hello {

	public static void main(String[] args) {
		System.out.println("Hello MyDrive!");
	}
	public static void bye(String[] args) {
		System.out.println("Goodbye MyDrive!");
	}
	public static void greet(String[] args) {
		System.out.println("Hello "+args[0]);
	}
	public static void execute(String[] args) {
		for (String s: args)
			System.out.println("Execute "+args[0]+"?");
	}
	public static void sum(String[] args) {
		int sum = 0;
		for (String s: args) sum += Integer.parseInt(s);
		System.out.println("sum="+sum);
	}
}
