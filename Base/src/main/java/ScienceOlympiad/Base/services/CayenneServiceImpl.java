package ScienceOlympiad.Base.services;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.configuration.server.ServerRuntime;


public class CayenneServiceImpl implements CayenneService{
	private ServerRuntime serverRuntime;
	private ObjectContext sharedContext;
	
	public CayenneServiceImpl()
	{
		serverRuntime = new ServerRuntime("cayenne-Science_Olympiad.xml");
		sharedContext = serverRuntime.getContext();
	}
	
	public ObjectContext sharedContext()
	{
		return sharedContext;
	}
	
	public ObjectContext newContext()
	{
		return serverRuntime.getContext();
	}

}
