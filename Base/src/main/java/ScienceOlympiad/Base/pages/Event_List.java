//Created by Dylan Visto
package ScienceOlympiad.Base.pages;

import java.util.List;

import org.apache.cayenne.Cayenne;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.query.SelectQuery;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.example.cayenne.persistent.Event;
import org.example.cayenne.persistent.Facilitator;
import org.example.cayenne.persistent.Division;

import ScienceOlympiad.Base.services.CayenneService;

public class Event_List {

	//Injecting the Cayenne Service
	@Inject
	private CayenneService cayenneService;
		
	ObjectContext context = cayenneService.newContext();
	
	@InjectPage
	private Event_List eventList;
	
	@InjectPage
	private Event_Division eventDivision;
	
	@Property
	private Division testDivision;
	
	@Property
	private List<Event> storeevents;
	
	@Property
    private Event eventadd;
	
	@Property
	private Event rowevent; 
	
	@Property
	@Persist
	private Integer linkedDivisionPK;
	
	@Property
	@Persist
	private String linkedDivisionPKString;
	
	@Property
	@Persist
	private String divisionName;
	
	@Property
	private List<Event> showEvents1;
	
	//sets the divisionPK value from the previous event division page
	public void set(Integer linkedDivisionPK) {
		this.linkedDivisionPK = linkedDivisionPK;
	}
	
	//Gets the name of the division selected
	void onActivate(Integer linkedDivisionPK) {
		this.linkedDivisionPK = linkedDivisionPK;
		Division d = (Division) Cayenne.objectForPK(context, Division.class, linkedDivisionPK);
		this.divisionName = d.getName();
	}
	
	Integer onPassivate() {
        return linkedDivisionPK;
    }
	
	void setupRender()
	{
		linkedDivisionPKString = Integer.toString(linkedDivisionPK);
   }
	
	//Gets a list of the events associated with the divisionPK value
	public List<Event> getRelatedEvents(){
		ObjectContext context = cayenneService.newContext();
		SelectQuery q = new SelectQuery(Event.class, ExpressionFactory.likeExp(Event.DIVISION_PROPERTY, linkedDivisionPKString));
		return context.performQuery(q);
	}
	
	//Deletes an event from the database
	void onDeleteEvent(int eventPK) {
		ObjectContext context = cayenneService.newContext();
		Event event = (Event) Cayenne.objectForPK(context, Event.class, eventPK);
		if(event != null)
		{
			context.deleteObject(event);
			context.commitChanges();
		}
	}
	
	//When creating an event, this links the event to the division that was selected on the previous page
	Object onSuccess() {
		Division linkedDivision = (Division) Cayenne.objectForPK(context, Division.class, linkedDivisionPK);
		eventadd.setDivision(linkedDivision);
		context.commitChanges();
		return eventList;
	}
	
	//allows the user to return to the eventDivision page
	Object onReturn() {
		return eventDivision;
	}
	
	
	
}
