package org.example.cayenne.persistent;

import org.example.cayenne.persistent.auto._Competition;

public class Competition extends _Competition {
	
	public Integer getPK()
	{
		if(getObjectId() != null && !getObjectId().isTemporary())
		{
			return (Integer) getObjectId().getIdSnapshot().get(ID_PK_COLUMN);
		}
		return null; 
	}

	
}
