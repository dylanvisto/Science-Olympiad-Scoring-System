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
import org.example.cayenne.persistent.Division;
import org.example.cayenne.persistent.Event;
import org.example.cayenne.persistent.School;
import ScienceOlympiad.Base.services.CayenneService;

public class School_List {

		@Inject
		private CayenneService cayenneService;
			
		ObjectContext context = cayenneService.newContext();
		
		@InjectPage
		private School_List schoolList;
		
		@InjectPage
		private School_Division schoolDivision;
		
		@Property
		private List<School> storeschools;
		
		@Property
		private School rowschool; 
		
		@Property
		@Persist
		private String divisionName;
		
		@Property
		@Persist
		private Integer linkedDivisionPK;
		
		@Property
		@Persist
		private String linkedDivisionPKString;
		
		@Property
		private School schooladd;
		
		//Sets the divisionPK value from the previous event division page
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
		
		//Gets a list of the schools associated with the divisionPK value
		public List<School> getRelatedSchools(){
			ObjectContext context = cayenneService.newContext();
			SelectQuery q = new SelectQuery(School.class, ExpressionFactory.likeExp(School.DIVISION_PROPERTY, linkedDivisionPKString));
			return context.performQuery(q);
		}
		
		//Deletes a school from the database
		void onDeleteSchool(int schoolPK) {
			ObjectContext context = cayenneService.newContext();
			School school = (School) Cayenne.objectForPK(context, School.class, schoolPK);
			if(school != null)
			{
				context.deleteObject(school);
				context.commitChanges();
			}
		}
		
		//When creating an school, this links the school to the division that was selected on the previous page
		Object onSuccess() {
			Division linkedDivision = (Division) Cayenne.objectForPK(context, Division.class, linkedDivisionPK);
			schooladd.setDivision(linkedDivision);
			context.commitChanges();
			return schoolList;
		}
		
		//allows the user to return to the eventDivision page
		Object onReturn() {
			return schoolDivision;
		}
	
	
	
}
