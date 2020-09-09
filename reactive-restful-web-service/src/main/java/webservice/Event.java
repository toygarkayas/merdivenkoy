package webservice;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Event {
    private String eventId;
    private String eventDt;
    
    public Event(String eventId, String eventDt) {
    	this.eventId=eventId;
    	this.eventDt=eventDt;
    }
    
    public String getEventId() {return eventId;}
    public String getEventDt() {return eventDt;}
    
}