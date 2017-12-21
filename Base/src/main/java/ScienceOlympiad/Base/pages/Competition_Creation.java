//Created by Austin Tadman; Modified by Dylan Visto to work with the database.
package ScienceOlympiad.Base.pages;

import java.util.List;
import org.apache.cayenne.Cayenne;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.query.SelectQuery;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.example.cayenne.persistent.Competition;
import ScienceOlympiad.Base.services.CayenneService;

public class Competition_Creation {

	@Inject
	private CayenneService cayenneService;
		
	ObjectContext context = cayenneService.newContext();
	
	@InjectPage
	private Edit_Competition editCompetitionPage;
	
	@InjectPage
	private School_Division schoolDivisionPage;
	
	@InjectPage
	private Event_Division eventDivisionPage;
	
	@InjectPage
	private Competition_Creation competitionCreationPage;
	
	@Property
	private Competition competition;
	
	@Property
	private Competition rowcompetition; 
	
	@Property
	@Persist
	private String selectedName;
	
	@Property
    private String newName;
	
	@Property
	@Persist
	private Integer competitionPK;
	
	@SessionState
	@Persist
	static Integer competitionOffpagePK;
	
	@InjectComponent("newCompForm")
    private Form form;
	
	@InjectComponent("newName")
    private TextField newNameField;
	
	//Gets all the competitions from the database
	public List<Competition> getRelatedCompetitions(){
		ObjectContext context = cayenneService.newContext();
		SelectQuery q = new SelectQuery(Competition.class);
		return context.performQuery(q);
	}
	
	//allows for deletion of a competition
	void onDelete(int competitionPK) {
		ObjectContext context = cayenneService.newContext();
		Competition competition = (Competition) Cayenne.objectForPK(context, Competition.class, competitionPK);
		if(competition != null)
		{
			//delete object from database
			context.deleteObject(competition);
			context.commitChanges();
		}
	}
	
	void onValidateFromNewCompForm() {
        if (newName == null) {
            form.recordError(newNameField, "Please specify a name for the new competition.");
        }
    }
	//event handler for the AssignJudges button for each row in the grid. Sets the offpageCompetitionPK value for the editCompetition page
	//so that it can get all the Judges associated with a competition
	Object onAssignJudges(int compPK) {
		competitionPK = compPK;
		competitionOffpagePK = competitionPK;
		editCompetitionPage.setcomp(competitionPK);
		return editCompetitionPage;
	}
	
	//Creates a new competition in the database
	Object onSuccess() {
		// create new competition with this name and add to table
		ObjectContext context = cayenneService.newContext();
		Competition newComp = context.newObject(Competition.class);
		newComp.setName(newName);
		context.commitChanges();
        return this;
    }
}
