package ScienceOlympiad.Base.pages;

import org.apache.cayenne.Cayenne;
import org.apache.cayenne.ObjectContext;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.BeanEditForm;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.example.cayenne.persistent.Event;

import ScienceOlympiad.Base.services.CayenneService;

public class Edit_Event {

	@Inject
	private CayenneService cayenneService;
	
	ObjectContext context = cayenneService.newContext();
	
	@Property
	private Event event;
	
	@Property
    private int eventId;

    @InjectPage
    private Event_List eventPage;

    @InjectComponent("editForm")
    private BeanEditForm editForm;
    
    int onPassivate() {
        return eventId;
    }
    
    void onActivate(int pk) {
        this.eventId = pk;
    }

    void setupRender() {
    	event = (Event) Cayenne.objectForPK(context, Event.class, eventId);
    }

    void onPrepareForRender() { }

    void onPrepareForSubmit() { }

    void onValidateFromEditForm() {
        if (editForm.getHasErrors()) {
            return;
        }

        try {
        	Event e = (Event) Cayenne.objectForPK(context, Event.class, eventId);
        	e.setName(event.getName());
            context.commitChanges();
        } catch (Exception e) {
            editForm.recordError(e.toString());
        }
    }

    Object onSuccess() {
        return eventPage;
    }
}
