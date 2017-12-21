package ScienceOlympiad.Base.pages;


import org.apache.cayenne.Cayenne;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.query.SelectQuery;

//Page Description
//---------------------------------
//This page seems pretty fleshed out as is. If whoever is in charge of the
//login page could get this to work, that would be awesome

import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.PasswordField;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.example.cayenne.persistent.Facilitator;

import ScienceOlympiad.Base.pages.Reports;
import org.slf4j.Logger;

import ScienceOlympiad.Base.services.CayenneService;

public class Login
{
	@Inject
	private CayenneService cayenneService;
	
	private ObjectContext context = cayenneService.newContext();
	
  @Inject
  private Logger logger;

  @Inject
  private AlertManager alertManager;

  @InjectComponent
  private Form login;
  
  @InjectComponent("userName")
  private TextField userNameField;
  
  @InjectComponent("password")
  private PasswordField passwordField;

  @Property
  private String userName;

  @Property
  private String password;
  
  @SessionState
  @Property
  private Facilitator user;

  //validates user and password
  void onValidateFromLogin()
  {
	Expression qualifier = ExpressionFactory.matchExp(Facilitator.USERNAME_PROPERTY, userName);
	SelectQuery select = new SelectQuery(Facilitator.class, qualifier);
	user = (Facilitator) Cayenne.objectForQuery(context, select);
    if ( user == null) {
    	login.recordError(userNameField, "No account matches with given user name.");
    }
    
    if ( !password.equals(user.getPassword()) ) {
    	login.recordError(passwordField, "Password does not match user name's account.");
    }
    else
    {
    	//Hash password and add salt
    	String hashedPassword = MD5(password + "X0QODHVtzCXmniR");
    }
  }

  //When login is successful
  Object onSuccessFromLogin()
  {
    logger.info("Login successful!");
    alertManager.success("Welcome aboard!");
    
    return Reports.class;
  }
  
  //when login is unsuccessful
  void onFailureFromLogin()
  {
    logger.warn("Login error!");
    alertManager.error("I'm sorry but I can't log you in!");
  }

  /*
   * Generages MD5 hash sum used for storing passwords a little more securely 
   */
  public String MD5(String md5) 
  {
	   try {
	        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
	        byte[] array = md.digest(md5.getBytes());
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < array.length; ++i) {
	        	sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
	        }
	        return sb.toString();
	   } catch (java.security.NoSuchAlgorithmException e) {
	   }
	   return null;
	}
}
