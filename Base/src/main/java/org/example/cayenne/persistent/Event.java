package org.example.cayenne.persistent;

import org.example.cayenne.persistent.auto._Event;

public class Event extends _Event {
	
	public Integer getPK()
	{
		if(getObjectId() != null && !getObjectId().isTemporary())
		{
			return (Integer) getObjectId().getIdSnapshot().get(ID_PK_COLUMN);
		}
		return null; 
	}
}
