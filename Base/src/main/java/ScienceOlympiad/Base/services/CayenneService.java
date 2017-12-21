package ScienceOlympiad.Base.services;

import org.apache.cayenne.ObjectContext;

public interface CayenneService 
{
	ObjectContext sharedContext();
	
	ObjectContext newContext();
	
}
