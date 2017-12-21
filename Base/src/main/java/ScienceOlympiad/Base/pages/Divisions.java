package ScienceOlympiad.Base.pages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import org.apache.cayenne.Cayenne;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.query.SelectQuery;
import org.apache.commons.io.IOUtils;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.ActionLink;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Response;
import org.example.cayenne.persistent.*;

import ScienceOlympiad.Base.Interfaces.OutputStreamResponse;
import ScienceOlympiad.Base.services.CayenneService;

public class Divisions {
	@Property
	private org.example.cayenne.persistent.Division division;
	
	@Property
	private List<School> teams;
	
	@Property
	private School team;
	
	@Property
	List<Event> events;
	
	@Property
	Event event;
	
	@Component(id="downloadLink")
	private ActionLink downloadLink;
	
	@Inject
	private CayenneService cayenneService;
	
	private ObjectContext context;
	
	
	@SuppressWarnings("unchecked")
	void onActivate(int PK){
		context = cayenneService.newContext();
		try {
			//Get division from PK
			division = (Division)Cayenne.objectForPK(context, Division.class, PK);
		}catch(Exception e) {
			e.printStackTrace();
		}
		//Get teams and divisions
		teams = division.getSchools();
		events = division.getEvents();
		Scores test = new Scores();
		test.getEventId();
		
	}
	//get the judge for each event
	public String getJudgeFromEvent(Event event) {
		final StringBuilder stringbuilder = new StringBuilder();
		Consumer<? super EventRoster> action = new Consumer<EventRoster>() {
			@Override
			public void accept(EventRoster X) {
				X.getFacilitator().forEach(new Consumer<Facilitator>() {
					@Override
					public void accept(Facilitator X) {stringbuilder.append(X.getFirstname()); stringbuilder.append(X.getLastname());}
				});
			}
		};
		event.getJudge().forEach(action);
		return stringbuilder.toString();
	}
	
	//Not working right now
	private File generateFile() {
		try {
			File temp = File.createTempFile("temp", ".xls");
			FileWriter  writer = new FileWriter(temp);
			writer.write("Hello World");
			writer.close();
			return temp;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	//not working right now
	@OnEvent(component="downloadLink")
	private Object handleDownload(){
		final File file = generateFile();
		final OutputStreamResponse response = new OutputStreamResponse() {
			public String getContentType() {
	            return "application/vnd.ms-excel"; // or whatever content type your file is
	        }

	        public void prepareResponse(Response response) {
	            response.setHeader ("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
	        }

	        @Override
	        public void writeToStream(OutputStream out) throws IOException {
	            try {
	                InputStream in = new FileInputStream(file);
	                IOUtils.copy(in,out);
	                in.close();
	                file.deleteOnExit();
	            } catch (Exception e) {
	                throw new RuntimeException(e);
	            }                   
	        }
		};
		return response;
		
	}
}
