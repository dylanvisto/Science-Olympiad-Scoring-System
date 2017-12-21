package ScienceOlympiad.Base.pages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import org.example.cayenne.persistent.Event;
import org.example.cayenne.persistent.School;
import org.example.cayenne.persistent.Scores;

import ScienceOlympiad.Base.Interfaces.OutputStreamResponse;
import ScienceOlympiad.Base.services.CayenneService;

public class Teams {
	@Property
	School school;
	
	@Property
	private List<Scores> scores;
	
	@Property
	private Scores score;
	
	@Component(id="downloadLink")
	private ActionLink downloadLink;
	
	@Inject
	private CayenneService cayenneService;
	
	private ObjectContext context;
	
	//When page is loaded
	@SuppressWarnings("unchecked")
	void onActivate(final int PK){
		context = cayenneService.newContext();
		try {
			school = (School)Cayenne.objectForPK(context, School.class, PK);
		}catch(Exception e) {
			System.out.println("Unable to get school with PK "+PK);
			e.printStackTrace();
		}
		//Get every score
		SelectQuery allScores = new SelectQuery(Scores.class);
		List<Scores> tempScores;
		try {
			tempScores = context.performQuery(allScores);
			System.out.println("Looking though "+tempScores.size()+" scores");
			Consumer<? super Scores> action = new Consumer<Scores>() {
				@Override // If score has the same id as the school pk then save to list of scores.
				public void accept(Scores X) {if (X.getSchoolId() == PK) scores.add(X);}
			};
			tempScores.forEach(action);
		}catch(Exception e) {
			//e.printStackTrace();
			System.out.println("Unable to get scores");
		}
	}
	
	public String eventNameFromScore(Scores score) {
		return Cayenne.objectForPK(context, Event.class, score.getEventId()).getName();
	}
	
	//Not working
	private File generateFile() {
		try {
			File temp = File.createTempFile("temp", ".xls");
			FileWriter  writer = new FileWriter(temp);
			writer.write("Hello World");
			writer.close();
			return temp;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to create file");
		}
		return null;
	}
	
	//Not working  
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
