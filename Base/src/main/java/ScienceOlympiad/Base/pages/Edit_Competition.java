//Skeleton created by Nick Carpenter; Modified by Dylan Visto to work with the database.
package ScienceOlympiad.Base.pages;

import java.util.List;
import org.apache.cayenne.Cayenne;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.query.SelectQuery;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.SelectModelFactory;
import org.example.cayenne.persistent.Competition;
import org.example.cayenne.persistent.Division;
import org.example.cayenne.persistent.Event;
import org.example.cayenne.persistent.EventRoster;
import org.example.cayenne.persistent.Facilitator;

import ScienceOlympiad.Base.services.CayenneService;

public class Edit_Competition {

	//This inner class is used inside the select component as the Encoder. The encoder converts between the event objects represented 
	//as options in the menu and the client-side encoded values that uniquely identify them.
	public ValueEncoder<Event> geteventEncoder() { 
			
			return new ValueEncoder<Event>() { 
				
				@Override 
				public String toClient(Event value) {
					
					return Integer.toString(value.getPK()); } 
				
				@Override 
				public Event toValue(String id) { 
					
					intHold = Integer.parseInt(id);
					
					selectedEvent = (Event) Cayenne.objectForPK(context, Event.class, intHold);
					
					return selectedEvent; } }; 
		}
	
	@Property
	private Integer intHold = 0;

	//Used in the select component to hold the value
	@Property
	private Event selectedEvent;
	
	@Inject
	private CayenneService cayenneService;
	
	ObjectContext context = cayenneService.newContext();
	
	@Property
	private Facilitator rowjudge;
	
	@Property
	private List<Facilitator> judges;
	
	@Property
	private String compName;
	
	@Property
	@Persist
	private Integer competitionOffpagePK;
	
	@Property
	@Persist
	private String competitionOffpagePKString;
	
	@SessionState
	@Persist
	static Integer judgeOffpagePK;
	
	@Property
	@Persist
	private Integer selectedJudgePK;
	
	@Property
	@Persist
	private Integer judgePK;
	
	@Property
	@Persist
	private String selectedFirstName;
	
	@Property
	@Persist
	private String selectedLastName;
	
	@Property
	private Facilitator newJudge;
	
	@InjectPage
	private View_Judge_Events viewJudgeEvents;
	
	@InjectPage
	private Edit_Competition editCompetition;
	
	@InjectPage
	private Competition_Creation createCompetition;
	
	@Property
	private Integer eventPK = null;
	
	//Model used by the select component that has a label (name in this case) and a value (the event object)
	@Property 
	private SelectModel eventSelectModel;
		
	//Used to create the select model
	@Inject 
	SelectModelFactory selectModelFactory;
	
	@Property
	private List<Event> storeEvents;
	
	
	void onValidateFromEventSelect() {
        eventPK = selectedEvent == null ? null : selectedEvent.getPK();
    }
	
	@SuppressWarnings("unchecked")
	void onPrepareFromEventSelect() {
		
		SelectQuery allEvents = new SelectQuery(Event.class);
		
		//Database query that gets all events in the event class and stores them in a list
		storeEvents = context.performQuery(allEvents);
		
		//Finds the selected event in the list. Probably not needed
		if (eventPK != null) {
	        selectedEvent = findEventInList(eventPK, storeEvents);
	    }
		
		//Creates a model for the select component
		eventSelectModel = selectModelFactory.create(storeEvents, "name");

    }
	
	//Searches for an event in the list. Probably not needed
	private Event findEventInList(Integer eventPK, List<Event> storeEvents) {
	        for (Event event : storeEvents) {
	            if (event.getPK().equals(eventPK)) {
	                return event;
	            }
	        }
	        return null;
	    }
	
	//Event handler for viewing the events associated with a judge. Takes the user to the View Judge Events page and sets the judgePK value on that page.
	Object onViewJudge(int thisjudgePK) {
		judgePK = thisjudgePK;
		judgeOffpagePK = judgePK;
		viewJudgeEvents.setfacPK(judgePK);
		//return editCompetition;
		return viewJudgeEvents;
	}
	
	//Creates the Event Roster Table entries when you select an event to be associated with a Facilitator
	Object onSuccessFromEventSelect() {
		EventRoster eventRoster = context.newObject(EventRoster.class);
		Facilitator j = (Facilitator) Cayenne.objectForPK(context, Facilitator.class, selectedJudgePK);
		Event e = (Event) Cayenne.objectForPK(context, Event.class, eventPK);
		eventRoster.addToEvent(e);
		eventRoster.addToFacilitator(j);
		eventRoster.setEventId(eventPK);
		eventRoster.setFacilitatorId(selectedJudgePK);
		context.commitChanges();
		return editCompetition;
 }
	
	//Sets the competitionOffpagePK to be the unqiue PK value associated with a competition
	public void setcomp(Integer competitionOffpagePK) {
		this.competitionOffpagePK = competitionOffpagePK;
	}
	
	void onActivate(Integer competitionOffpagePK) {
		this.competitionOffpagePK = competitionOffpagePK;
	}
	
	Integer onPassivate() {
        return competitionOffpagePK;
    }
	
	void setupRender()
	{
		
    }
	
	//Gets all the facilitators associated with the competitionPK value
	public List<Facilitator> getRelatedJudges(){
		SelectQuery q = new SelectQuery(Facilitator.class, ExpressionFactory.likeExp(Facilitator.COMPETITION_PROPERTY, competitionOffpagePK));

		return context.performQuery(q);
	}
	
	//Probably not needed now due to the method above
	private void onActivate(int compPK) {
		Competition comp = (Competition) Cayenne.objectForPK(context, Competition.class, compPK);
		if(comp != null)
		{
			compName = comp.getName();
	        judges = comp.getJudges();
		}
    }
	
	//Deletes a facilitator from the database and removes it from the list
	private void onDeleteJudge(int judgePK) {
		Facilitator j = (Facilitator) Cayenne.objectForPK(context, Facilitator.class, judgePK);
		if(j != null)
		{
			context.deleteObject(j);
			context.commitChanges();
		}
	}
	
	//A selected judge can be assigned an event. This method is the event handler for selecting a judge
	private void onSelectJudge(int judgePK) {
		Facilitator j = (Facilitator) Cayenne.objectForPK(context, Facilitator.class, judgePK);
		this.selectedFirstName = j.getFirstname();
		this.selectedLastName = j.getLastname();
	}
	
	//When creating a new judge, this links that judge to the competition
	Object onSuccessFromJudgeForm() {
		Competition linkedCompetition = (Competition) Cayenne.objectForPK(context, Competition.class, competitionOffpagePK);
		newJudge.setCompetition(linkedCompetition);
		context.commitChanges();
		return editCompetition;
	}
	
	//Allows the user to return to the previous createCompetition page
	Object onReturn() {
		return createCompetition;
	}
}
