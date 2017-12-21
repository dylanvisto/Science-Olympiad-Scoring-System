package org.example.cayenne.persistent.auto;

import java.util.List;

import org.apache.cayenne.CayenneDataObject;
import org.example.cayenne.persistent.Competition;
import org.example.cayenne.persistent.Event;
import org.example.cayenne.persistent.School;

/**
 * Class _Division was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _Division extends CayenneDataObject {

    public static final String NAME_PROPERTY = "name";
    public static final String COMPETITION_PROPERTY = "competition";
    public static final String EVENTS_PROPERTY = "events";
    public static final String SCHOOLS_PROPERTY = "schools";

    public static final String ID_PK_COLUMN = "ID";

    public void setName(String name) {
        writeProperty(NAME_PROPERTY, name);
    }
    public String getName() {
        return (String)readProperty(NAME_PROPERTY);
    }

    public void setCompetition(Competition competition) {
        setToOneTarget(COMPETITION_PROPERTY, competition, true);
    }

    public Competition getCompetition() {
        return (Competition)readProperty(COMPETITION_PROPERTY);
    }


    public void addToEvents(Event obj) {
        addToManyTarget(EVENTS_PROPERTY, obj, true);
    }
    public void removeFromEvents(Event obj) {
        removeToManyTarget(EVENTS_PROPERTY, obj, true);
    }
    @SuppressWarnings("unchecked")
    public List<Event> getEvents() {
        return (List<Event>)readProperty(EVENTS_PROPERTY);
    }


    public void addToSchools(School obj) {
        addToManyTarget(SCHOOLS_PROPERTY, obj, true);
    }
    public void removeFromSchools(School obj) {
        removeToManyTarget(SCHOOLS_PROPERTY, obj, true);
    }
    @SuppressWarnings("unchecked")
    public List<School> getSchools() {
        return (List<School>)readProperty(SCHOOLS_PROPERTY);
    }


}
