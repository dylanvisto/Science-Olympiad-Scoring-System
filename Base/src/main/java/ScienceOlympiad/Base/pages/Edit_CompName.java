package ScienceOlympiad.Base.pages;

import org.apache.cayenne.Cayenne;
import org.apache.cayenne.ObjectContext;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.BeanEditForm;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.example.cayenne.persistent.Competition;

import ScienceOlympiad.Base.services.CayenneService;

public class Edit_CompName {

	@Inject
	private CayenneService cayenneService;
	
	ObjectContext context = cayenneService.newContext();
	
	@Property
	private Competition competition;
	
	@Property
    private int compId;

    @InjectPage
    private Competition_Creation compPage;

    @InjectComponent("editForm")
    private BeanEditForm editForm;
    
    int onPassivate() {
        return compId;
    }
    
    void onActivate(int pk) {
        this.compId = pk;
    }

    void setupRender() {
    	competition = (Competition) Cayenne.objectForPK(context, Competition.class, compId);
    }

    void onPrepareForRender() { }

    void onPrepareForSubmit() { }

    void onValidateFromEditForm() {
        if (editForm.getHasErrors()) {
            return;
        }

        try {
        	Competition c = (Competition) Cayenne.objectForPK(context, Competition.class, compId);
        	c.setName(competition.getName());
            context.commitChanges();
        } catch (Exception e) {
            editForm.recordError(e.toString());
        }
    }

    Object onSuccess() {
        return compPage;
    }
}
