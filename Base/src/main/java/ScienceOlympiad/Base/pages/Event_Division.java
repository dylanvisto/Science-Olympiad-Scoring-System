package ScienceOlympiad.Base.pages;

import java.util.List;

import org.apache.cayenne.Cayenne;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.query.SelectQuery;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.SelectModelFactory;
import org.example.cayenne.persistent.Division;

import ScienceOlympiad.Base.services.CayenneService;

//Page Description
//-------------------------------------
//- This page holds a drop down component to select a division. Division B and C (Junior and Senior Division) are created before the page renders so they can be listed in the drop down component.
//- A continue button will then take you to the following page: Event_List


public class Event_Division {

	//This inner class is used inside the select component as the Encoder. The encoder converts between the division objects represented 
		//as options in the menu and the client-side encoded values that uniquely identify them.	
public ValueEncoder<Division> getdivisionEncoder() { 
		
		return new ValueEncoder<Division>() { 
			
			@Override 
			public String toClient(Division value) {
				
				return Integer.toString(value.getPK()); } 
			
			@Override 
			public Division toValue(String id) { 
				
				intHold = Integer.parseInt(id);
				
				selectedDivision = (Division) Cayenne.objectForPK(context, Division.class, intHold);
				
				
				
				return selectedDivision; } }; 
	}
	
	
	@Property
	private Integer intHold = 0;
	
	//Injects the Event_Data page so that it can be accessed after form submission
	@InjectPage
	private Event_List eventList;
	
	//Model used by the select component that has a label (name in this case) and a value (the division object)
	@Property 
	private SelectModel divisionSelectModel;
	
	//Used to create the select model
	@Inject 
	SelectModelFactory selectModelFactory;
	
	//List of type Division that holds each division entry from the database
	@Property
	private List<Division> storeDivisions;
	
	
	static Division offPageDivision;
	
	//Used in the select component to hold the value
	@Property
	//static
	//@SessionState
	private Division selectedDivision;
	
	//Stores the division PK value
	@Property
	private Integer divisionPK = null;
	
	//Variable that is used across pages that gets the PK value of the most recent menu selection (1 - Senior Division, 2 - Junior Division)
	@Property
	private Integer eventDivisionOffpagePK = null;
	
	//Injecting the Cayenne Service
	@Inject
	private CayenneService cayenneService;
	
	ObjectContext context = cayenneService.newContext();
	
	
	void onValidateFromForm() {
        divisionPK = selectedDivision == null ? null : selectedDivision.getPK();
    }
	
	//Kind of like the setupRender() method but works with a form
	@SuppressWarnings("unchecked")
	void onPrepare() {
		
		SelectQuery allDivisions = new SelectQuery(Division.class);
		
		//Database query that gets all divisions in the division class and stores them in a list
		storeDivisions = context.performQuery(allDivisions);
		
		//Finds the selected division in the list. Probably not needed
		if (divisionPK != null) {
	        selectedDivision = findDivisionInList(divisionPK, storeDivisions);
	    }
		
		//Creates a model for the select component
		divisionSelectModel = selectModelFactory.create(storeDivisions, "name");

    }
	
	//Form submission event handler. When you press submit, this event handler will run
	Object onSuccess() {
			//stores current divisionPK value from the selected menu option and stores it in a variable that 
			//can be used across multiple pages
			eventDivisionOffpagePK = divisionPK;
			eventList.set(eventDivisionOffpagePK);
			//returns to the school_data page
			return eventList;
		
	 }
	
	//Searches for a division in the list. Probably not needed
	private Division findDivisionInList(Integer divisionPK, List<Division> storeDivisions) {
        for (Division division : storeDivisions) {
            if (division.getPK().equals(divisionPK)) {
                return division;
            }
        }
        return null;
    }
	
	
}
