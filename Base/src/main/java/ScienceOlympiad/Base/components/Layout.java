package ScienceOlympiad.Base.components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.query.SelectQuery;
import org.apache.tapestry5.*;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.ioc.annotations.*;
import org.apache.tapestry5.services.Response;
import org.example.cayenne.persistent.Competition;
import org.example.cayenne.persistent.Facilitator;

import ScienceOlympiad.Base.services.*;

import ScienceOlympiad.Base.pages.Reports;
import ScienceOlympiad.Base.pages.Login;


//@Import(module="bootstrap/collapse")
@Import(stylesheet="context:css/BasicStyling.css")
public class Layout
{
  @Inject
  private ComponentResources resources;
  
  @Inject
  private CayenneService cayenneService;
	
  private ObjectContext context = cayenneService.newContext();

  @Property
  @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
  private String title;

  @Property
  private String pageName;
  
  @Property
  private String loginButtonContent;
  
  @SessionState
  @Property
  private Facilitator user;
  
  @Inject
  private Response response;
  
  //Set navigation privileges in these fields.
  private String[] nonLoginPages = {"Index", "About", "Reports"};
  //Add judge scoring panel here when completed
  private String[] judgePages = {};
  private String[] mainJudgePages = {"Competition_Creation", "Event_Division", "School_Division", "Edit_Competition", "View_Judge_Events", "School_List", "Event_List", "Events", "Divisions","Teams","Reports","Edit_Judge", "Edit_CompName", "Edit_Event", "Edit_School", "JudgeEvent", "JudgeEvent2"};
  private String[] mainJudgePagesView = {"Competition_Creation", "Event_Division", "School_Division"};

  @Property
  @Inject
  @Symbol(SymbolConstants.APPLICATION_VERSION)
  private String appVersion;
  
  public void beginRender() throws IOException{

	//Checks if user as access privilege to the page they are trying to navigate to.
	if(!checkNavigationAccess()) {
		response.sendRedirect("Index");
	}
  }
  
  public void setupRender() {
	  
	  //Make some test users for logging in.
	  makeTestUsers();
	  
	  //Adjusts Sign In/Out button text to match user state.
	  if(user.getUsername() != null) {
		  loginButtonContent = "Sign Out";
	  }
	  else {
		  loginButtonContent = "Sign In";
	  }
  }
  
  /**
   * Adjusts the Sign In/Out buttons functionality based on user state.
   * @return Page
   */
  public Object onLoginSelect() {
	  if(user.getUsername() != null) {
		  user = null;
		  return Reports.class;
	  }
	  else {
		  return Login.class;
	  }
  }
  
  /**
   * Some default Tapestry project code used for assigning page navigation to nav bar buttons.
   * @return String
   */
  public String getClassForPageName()
  {
    return resources.getPageName().equalsIgnoreCase(pageName)
        ? "active"
        : null;
  }

  /**
   * Makes and returns an ArrayList<String> of all the pages a user has access to.
   * @return ArrayList<String>
   */
  public ArrayList<String> getPageNames()
  {
	    ArrayList<String> nav = new ArrayList<String>();
	    
	    nav.addAll( Arrays.asList(nonLoginPages));
	    
	    if(user.getUsername() != null) {
	    	//Change nonLoginPages here back to judgePages
	    	nav.addAll( Arrays.asList(judgePages));
	    	
	    	if(user.getMainJudge()) {
	        	nav.addAll( Arrays.asList(mainJudgePagesView));
	        }
	    }
	    return nav;
	  }
  
  /**
   * Verifies access of user to a page. Returns true if they have access privilege and false if they do not.
   * @return boolean
   */
  private boolean checkNavigationAccess() {
	  if(resources.getPageName().equalsIgnoreCase("Login")) {
		  return true;
	  }
	  
	  for(String page : nonLoginPages) {
		  if(resources.getPageName().equalsIgnoreCase(page)) {
			  return true;
		  }
	  }
	  
	  if(user.getUsername() != null) {
		  //Change nonLoginPages here back to judgePages
		  for(String page : judgePages) {
			  if(resources.getPageName().equalsIgnoreCase(page)) {
				  return true;
			  }
		  }
		  
		  if(user.getMainJudge())
		  {
			  for(String page : mainJudgePages) {
				  if(resources.getPageName().equalsIgnoreCase(page)) {
					  return true;
				  }
			  }
		  }
	  }
	  
	  return false;
  }
  
  /**
   * Makes some default users for testing purposes.
   */
  private void makeTestUsers() {
	  //Check if any users exist, and generate some if they don't.
	  SelectQuery allJudges = new SelectQuery(Facilitator.class);
	  if(context.performQuery(allJudges).isEmpty()) {
		  Facilitator main = context.newObject(Facilitator.class);
		  main.setMainJudge(true);
		  main.setFirstname("admin");
		  main.setUsername("admin");
		  main.setPassword("admin");
	  
  
		  
//		  Judge nonMain = context.newObject(Judge.class);
//		  nonMain.setMainJudge(false);
//		  nonMain.setUsername("user");
//		  nonMain.setPassword("user");
//		  
//		  Competition comp = context.newObject(Competition.class);
//		  comp.addToJudges(nonMain);
//		  comp.setName("Physics Catapult");
//		  
//		  Judge named = context.newObject(Judge.class);
//		  named.setMainJudge(false);
//		  named.setUsername("John Walker");
//		  named.setPassword("password");
//		  comp.addToJudges(named);
		  
		  context.commitChanges();
	  }
  }

}
