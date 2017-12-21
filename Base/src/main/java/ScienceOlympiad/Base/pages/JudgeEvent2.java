package ScienceOlympiad.Base.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.cayenne.Cayenne;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.query.SelectQuery;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.EventLink;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.example.cayenne.persistent.Division;
import org.example.cayenne.persistent.Facilitator;
//import org.example.cayenne.persistent.EventScores;
import org.example.cayenne.persistent.School;
import org.example.cayenne.persistent.SchoolScores;
import org.example.cayenne.persistent.Scores;

import ScienceOlympiad.Base.services.CayenneService;
//import ScienceOlympiad.Base.services.Event;
import ScienceOlympiad.Base.services.SchoolScore;

public class JudgeEvent2 {
	
	@Property
	private List<SchoolScore> schoolScores;
	
	@Property
	private SchoolScore schoolScore;
	
	@Inject
	private CayenneService cayenneService;
	private ObjectContext context = cayenneService.newContext();
	
	@InjectPage
	private JudgeEvent judgeEventPage;
	
	@Property
	@Persist
	private String selectedSchoolScore;
	
	@Property
	private Integer schoolPK;
	
	@InjectComponent("newScoreForm")
    private Form form;
	
	@InjectComponent("newScore")
    private TextField newScoreField;
	
	@Property
    private String newScore;
	
	
	void setupRender()
	{	
		schoolScores = new ArrayList<SchoolScore>();		
		for(int i = 1; i <= 5; i++)
		{
			SchoolScore schoolScore = new SchoolScore("School"+i, 0);
			schoolScores.add(schoolScore);
		}
//		context.commitChanges();
	}
	
	void onValidateFromNewScoreForm() {
        if (newScore == null) {
            form.recordError(newScoreField, "Please specify a name for the new competition.");
        }
    }
}
