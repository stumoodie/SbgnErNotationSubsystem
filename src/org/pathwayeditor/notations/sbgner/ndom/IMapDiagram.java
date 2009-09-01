package org.pathwayeditor.notations.sbgner.ndom;

import java.util.Iterator;

public interface IMapDiagram {
	String getName();
	
	Iterator<IBasicEntityNode> getBasicEntityNodes();
}
