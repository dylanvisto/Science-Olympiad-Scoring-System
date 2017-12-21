package ScienceOlympiad.Base.pages;

//Page Description
//---------------------------------------
//- The top three schools for each event should be listed here, depending on which event is viewed
//- A view full report button will take the judge to a large table of all the schools and their scores for each event. Two of these tables will correspond to each division (Junior B and Senior C).
// A download CSV file button is used to download a full report of both divisions and the scores.

//import java.io.*;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.ActionLink;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.example.cayenne.persistent.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import org.apache.cayenne.Cayenne;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.query.SelectQuery;
import org.apache.commons.io.IOUtils;
import org.apache.tapestry5.services.Response;

import ScienceOlympiad.Base.Interfaces.OutputStreamResponse;
import ScienceOlympiad.Base.services.CayenneService;
import ScienceOlympiad.Base.services.CayenneServiceImpl;

public class Reports {

	@Property
	private List<org.example.cayenne.persistent.Division> divisions;

	@Property
	private org.example.cayenne.persistent.Division division;

	@Property
	private List<org.example.cayenne.persistent.School> schools;

	@Property
	private List<org.example.cayenne.persistent.Event> events;

	@Property
	private List<org.example.cayenne.persistent.Scores> scores;

	@Component(id = "downloadLink")
	private ActionLink downloadLink;

	@Inject
	private CayenneService cayenneService;

	private ObjectContext context;

	@SuppressWarnings("unchecked")
	void setupRender() {
		context = cayenneService.newContext();
		// query to find divisions and scores
		SelectQuery allDivisions = new SelectQuery(org.example.cayenne.persistent.Division.class);
		SelectQuery allScores = new SelectQuery(Scores.class);
		scores = context.performQuery(allScores);
		try {
			// Set property of both division and scores
			divisions = (List<org.example.cayenne.persistent.Division>) context.performQuery(allDivisions);
			scores = (List<org.example.cayenne.persistent.Scores>) context.performQuery(allScores);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private File generateFile() throws IOException {
		try {
			context = cayenneService.newContext();
			File temp = File.createTempFile("reports", ".csv");
			FileWriter writer = new FileWriter(temp);
			SelectQuery allDivisions = new SelectQuery(org.example.cayenne.persistent.Division.class);
			SelectQuery allScores = new SelectQuery(Scores.class);
			divisions = (List<org.example.cayenne.persistent.Division>) context.performQuery(allDivisions);
			scores = (List<org.example.cayenne.persistent.Scores>) context.performQuery(allScores);
			for (Division div : divisions) {
				// get events and schools from the division
				events = div.getEvents();
				schools = div.getSchools();
				final HashMap<Integer, Integer> SchoolPKtoIndex = new HashMap<Integer, Integer>(
						(int) (schools.size() * 1.5));
				final HashMap<Integer, Integer> EventPKtoIndex = new HashMap<Integer, Integer>(
						(int) (events.size() * 1.5));
				final String[][] s = new String[schools.size() + 1][events.size() + 2];
				s[0][0] = div.getName();
				int index = 1;
				for (Event event : events) {
					s[0][index] = event.getName();
					EventPKtoIndex.put(event.getPK(), index++);
				}
				s[0][index] = "Total";
				index = 1;
				for (School school : schools) {
					s[index][0] = school.getName();
					SchoolPKtoIndex.put(school.getPK(), index++);
				}

				Consumer<? super Scores> action = new Consumer<Scores>() {
					@Override // Add to list of scores all scores that have the same event ID as the viewed
								// event PK
					public void accept(Scores X) {
						if (EventPKtoIndex.containsKey(X.getEventId())) {
							if (SchoolPKtoIndex.containsKey(X.getSchoolId())) {
								s[SchoolPKtoIndex.get(X.getSchoolId())][EventPKtoIndex.get(X.getEventId())] = X
										.getScore().toString();
							}
						}

					}
				};
				scores.forEach(action);
				for (int i = 0; i < (events.size() + 2); i++) {
					for (int j = 0; j < (schools.size() + 1); j++) {
						if (s[j][i] != null)
							writer.write(s[j][i]);
						writer.write(",");
					}
					writer.write("\n");
				}
				writer.write("\n\n");
			}
			writer.close();

			return temp;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			File temp = File.createTempFile("reports", ".csv");
			FileWriter writer = new FileWriter(temp);
			writer.write("ERROR");
			writer.close();
			return temp;
		}
		return null;

	}

	@OnEvent(component = "downloadLink")
	private Object handleDownload() throws IOException {
		final File file = generateFile();
		final OutputStreamResponse response = new OutputStreamResponse() {
			public String getContentType() {
				return "text/csv"; // or whatever content type your file is
			}

			public void prepareResponse(Response response) {
				response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
			}

			@Override
			public void writeToStream(OutputStream out) throws IOException {
				try {
					InputStream in = new FileInputStream(file);
					IOUtils.copy(in, out);
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
