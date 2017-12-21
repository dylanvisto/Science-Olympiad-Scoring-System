package ScienceOlympiad.Base.pages;

import org.apache.cayenne.Cayenne;
import org.apache.cayenne.ObjectContext;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.BeanEditForm;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.example.cayenne.persistent.Facilitator;

import ScienceOlympiad.Base.services.CayenneService;

public class Edit_Judge {

	@Inject
	private CayenneService cayenneService;
	
	ObjectContext context = cayenneService.newContext();
	
	@Property
	private Facilitator judge;
	
	@Property
    private int judgeId;

    @InjectPage
    private Edit_Competition compPage;

    @InjectComponent("editForm")
    private BeanEditForm editForm;
    
    int onPassivate() {
        return judgeId;
    }
    
    void onActivate(int pk) {
        this.judgeId = pk;
    }

    void setupRender() {
    	judge = (Facilitator) Cayenne.objectForPK(context, Facilitator.class, judgeId);
    }

    void onPrepareForRender() { }

    void onPrepareForSubmit() { }

    void onValidateFromEditForm() {
        if (editForm.getHasErrors()) {
            return;
        }

        try {
        	Facilitator j = (Facilitator) Cayenne.objectForPK(context, Facilitator.class, judgeId);
        	j.setFirstname(judge.getFirstname());
        	j.setLastname(judge.getLastname());
        	j.setMainJudge(judge.getMainJudge());
        	j.setPassword(judge.getPassword());
        	j.setUsername(judge.getUsername());
            context.commitChanges();
        } catch (Exception e) {
            editForm.recordError(e.toString());
        }
    }

    Object onSuccess() {
        return compPage;
    }
}