package org.example.cayenne.persistent;

import org.example.cayenne.persistent.auto._Division;

public class Division extends _Division {

	public Integer getPK()
	{
		if(getObjectId() != null && !getObjectId().isTemporary())
		{
			return (Integer) getObjectId().getIdSnapshot().get(ID_PK_COLUMN);
		}
		return null; 
	}

}