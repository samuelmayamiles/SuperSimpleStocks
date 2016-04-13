package main.java.model;

import java.util.LinkedList;
import java.util.List;

import main.java.bo.CommonStock;
import main.java.bo.PreferredStock;
import main.java.bo.impl.AbstractStock;

/**
 * Class to load values to the SuperSimpleStocks
 * 
 * @author Samuel Maya Miles
 * @version 1.0
 */
public class ValueLoader {

	/**
	 * Creates a dummy set of data (Should load objects from persistence in real
	 * life).
	 * 
	 * @return List<AbstractStock> List containing demo data.
	 */
	public List<AbstractStock> loadDummyValues() {
		List<AbstractStock> dummyList = new LinkedList<AbstractStock>();

		dummyList.add(new CommonStock("TEA", 0, 100));
		dummyList.add(new CommonStock("POP", 8, 100));
		dummyList.add(new CommonStock("ALE", 23, 60));
		dummyList.add(new PreferredStock("GIN", 8, 2, 100));
		dummyList.add(new CommonStock("JOE", 13, 250));

		return dummyList;
	}
}
