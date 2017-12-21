package org.example.cayenne.persistent;

import org.example.cayenne.persistent.auto._EventScores;

public class EventScores extends _EventScores {
	
	public Integer getPK()
	{
		if(getObjectId() != null && !getObjectId().isTemporary())
		{
			return (Integer) getObjectId().getIdSnapshot().get(ID_PK_COLUMN);
		}
		return null; 
	}


}
