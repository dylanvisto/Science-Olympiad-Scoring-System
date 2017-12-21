package ScienceOlympiad.Base.pages;

import org.apache.cayenne.Cayenne;
import org.apache.cayenne.ObjectContext;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.BeanEditForm;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.example.cayenne.persistent.School;

import ScienceOlympiad.Base.services.CayenneService;

public class Edit_School {

	@Inject
	private CayenneService cayenneService;
	
	ObjectContext context = cayenneService.newContext();
	
	@Property
	private School school;
	
	@Property
    private int schoolId;

    @InjectPage
    private School_List schoolPage;

    @InjectComponent("editForm")
    private BeanEditForm editForm;
    
    int onPassivate() {
        return schoolId;
    }
    
    void onActivate(int pk) {
        this.schoolId = pk;
    }

    void setupRender() {
    	school = (School) Cayenne.objectForPK(context, School.class, schoolId);
    }

    void onPrepareForRender() { }

    void onPrepareForSubmit() { }

    void onValidateFromEditForm() {
        if (editForm.getHasErrors()) {
            return;
        }

        try {
        	School s = (School) Cayenne.objectForPK(context, School.class, schoolId);
        	s.setName(school.getName());
        	s.setClassType(school.getClassType());
            context.commitChanges();
        } catch (Exception e) {
            editForm.recordError(e.toString());
        }
    }

    Object onSuccess() {
        return schoolPage;
    }
}
