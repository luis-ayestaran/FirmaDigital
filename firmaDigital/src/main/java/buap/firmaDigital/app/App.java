package buap.firmaDigital.app;

import buap.firmaDigital.vistas.FirmaDigital;

public class App {
	
	public static void main(String args[]) {
		
		init(args);
		
	}
	
	private static void init(String args[]) {
		
		new FirmaDigital().lanzarVista(args);
		
	}
	
}
