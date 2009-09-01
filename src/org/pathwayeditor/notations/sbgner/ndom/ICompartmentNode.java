package org.pathwayeditor.notations.sbgner.ndom;

import java.util.Iterator;

public interface ICompartmentNode {
	
	String getName();
	
	Iterator<IBasicEntityNode> nodeIterator();
}
