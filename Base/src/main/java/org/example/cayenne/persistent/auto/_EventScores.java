package org.example.cayenne.persistent.auto;

import org.apache.cayenne.CayenneDataObject;
import org.example.cayenne.persistent.School;

/**
 * Class _EventScores was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _EventScores extends CayenneDataObject {

    public static final String EVENT_NAME_PROPERTY = "eventName";
    public static final String EVENT_SCORE_PROPERTY = "eventScore";
    public static final String SCHOOL_PROPERTY = "school";

    public static final String ID_PK_COLUMN = "ID";

    public void setEventName(String eventName) {
        writeProperty(EVENT_NAME_PROPERTY, eventName);
    }
    public String getEventName() {
        return (String)readProperty(EVENT_NAME_PROPERTY);
    }

    public void setEventScore(Integer eventScore) {
        writeProperty(EVENT_SCORE_PROPERTY, eventScore);
    }
    public Integer getEventScore() {
        return (Integer)readProperty(EVENT_SCORE_PROPERTY);
    }

    public void setSchool(School school) {
        setToOneTarget(SCHOOL_PROPERTY, school, true);
    }

    public School getSchool() {
        return (School)readProperty(SCHOOL_PROPERTY);
    }


}