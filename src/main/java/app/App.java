package app;

import excepciones.NoHayCupoException;

public class App {

	public static void main(String[] args) throws NoHayCupoException {
		
		ParqueTierra parque = new ParqueTierra();
		parque.comenzarPrograma();
	}
}