package ScienceOlympiad.Base.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.cayenne.Cayenne;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.query.SelectQuery;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.EventLink;
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


public class JudgeEvent {
	
	@Property
	private org.example.cayenne.persistent.Division division;
	
	@Property
	private List<School> teams;
	
	@Property
	private School team;
	
	@Property
	@Persist
	private Integer teamPK;
	
	@Inject
	private CayenneService cayenneService;
	private ObjectContext context;
	
	@InjectPage
	private JudgeEvent judgeEventPage;
	
	@Property
	private List<SchoolScore> schoolScores;
	
	@Property
	private SchoolScore schoolScore;
	
	@SuppressWarnings("unchecked")
	void onActivate(int PK)
	{
		context = cayenneService.newContext();
		try {
			division = (Division)Cayenne.objectForPK(context, Division.class, PK);
		} catch(Exception e) {
			e.printStackTrace();
		}
		teams = division.getSchools();
	}
}
