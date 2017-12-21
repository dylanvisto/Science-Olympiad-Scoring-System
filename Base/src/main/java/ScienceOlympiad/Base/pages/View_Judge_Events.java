//Created by Dylan Visto - 12/06/17
//This page lists all of the events associated with a facilitator. A join table is used to relate events and facilitators together.
package ScienceOlympiad.Base.pages;

import java.util.ArrayList;
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
import org.example.cayenne.persistent.EventRoster;
import ScienceOlympiad.Base.services.CayenneService;

public class View_Judge_Events {

	@Inject
	private CayenneService cayenneService;
		
	ObjectContext context = cayenneService.newContext();
	
	@InjectPage
	private Edit_Competition editCompetition;
	
	@Property
	@Persist
	private Integer judgeOffpagePK;
	
	@Property
	private String judgeOffpagePKString;
	
	@Property
	private Event rowevent;
	
	@Property
	private Integer roweventroster;
	
	@Property
	private List<Event> eventList;
	
	
	@Property
	@Persist
	private List<EventRoster> storeEventRoster;
	
	@Property
	@Persist
	private List<Event> storeEvents;
	
	@Property
	@Persist
	private Integer eventID;
	
	@Property
	@Persist
	private List<Integer> storeEventID;
	
	//Sets the judgeOffpagePK from another page
	public void setfacPK(Integer judgeOffpagePK) {
		this.judgeOffpagePK = judgeOffpagePK;
	}
	
	//When the page starts loading up, the two arraylists are initialized and cleared
	void onActivate(Integer competitionOffpagePK) {
		this.judgeOffpagePK = judgeOffpagePK;
		storeEvents = new ArrayList<Event>();
		storeEventID = new ArrayList<Integer>();
		storeEventID.clear();
		storeEvents.clear();
	}
	
	Integer onPassivate() {
        return judgeOffpagePK;
    }
	
	//On page render, storeEventRoster is filled with all the Event_Roster entries with judgeOffpagePK as its facilitorID. The eventID's are then retrieved from the Event_Roster join table
	//and are used to grab all of the events from the Event table. Those events are then added to an arraylist that is used for the grid.
	void setupRender()
	{
		judgeOffpagePKString = Integer.toString(judgeOffpagePK);
		ObjectContext context = cayenneService.newContext();
		SelectQuery q = new SelectQuery(EventRoster.class, ExpressionFactory.likeExp(EventRoster.FACILITATOR_PROPERTY, judgeOffpagePK));
		storeEventRoster = context.performQuery(q);
		for(int i = 0; i < storeEventRoster.size(); i++) {
			storeEventID.add(storeEventRoster.get(i).getEventId());
			eventID = storeEventRoster.get(i).getEventId();
		}
		for(int j = 0; j < storeEventID.size(); j++) {
			Event event = (Event) Cayenne.objectForPK(context, Event.class, storeEventID.get(j));
			storeEvents.add(event);
		}
		
    }
	
	//Gets a list of Events from the storeEvents arraylist
	public List<Event> getRelatedEvents(){
		
		return storeEvents;
	}
	
	//Used for testing to display EventRoster elements
	public List<Integer> getRelatedEventRoster(){
		
		return storeEventID;
	}
	
	//Currently not working due to the delete rule also deleting the Facilitator
	void onDeleteEvent(int eventPK) {
//		for(int i = 0; i < storeEventRoster.size(); i++) {
//			if(eventPK == storeEventRoster.get(i).getEventId()) {
//				storeEventRoster.get(i).setEventId(1000);
//				storeEventRoster.get(i).setFacilitatorId(1000);
//				context.deleteObject(storeEventRoster.get(i));
//			}
//		}
//		context.commitChanges();
	}
	
	//Method to return the user to the editCompetition page
	Object onReturn() {
		return editCompetition;
	}
	
	
}
