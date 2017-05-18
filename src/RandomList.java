

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * ArrayList which allows to select and remove a random element
 * 
 * @param <T>
 */
public class RandomList<T> extends ArrayList<T>{
	
	private static final long serialVersionUID = -3851490836545904119L;

	/**
	 * Selects and removes a random element 
	 * @return the element
	 */
	public T removeRandom(){
		int randomIndex = ThreadLocalRandom.current().nextInt(0, size());
		return remove(randomIndex);
	}
}
