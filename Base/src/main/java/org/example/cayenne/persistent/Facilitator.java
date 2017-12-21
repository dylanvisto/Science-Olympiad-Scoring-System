package org.example.cayenne.persistent;

import org.example.cayenne.persistent.auto._Facilitator;

public class Facilitator extends _Facilitator {
	
	public Integer getPK()
	{
		if(getObjectId() != null && !getObjectId().isTemporary())
		{
			return (Integer) getObjectId().getIdSnapshot().get(ID_PK_COLUMN);
		}
		return null; 
	}

}
