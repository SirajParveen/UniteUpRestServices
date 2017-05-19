package com.niit.uniteup.rest.services;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.niit.uniteup.dao.EventDAO;
import com.niit.uniteup.model.Event;

@RestController
public class EventRestController {
	
	@Autowired
	private EventDAO eventDAO;

	@PostMapping("/createevent")
	public ResponseEntity<Event> createevent(@RequestBody Event event,HttpSession session){
		System.out.println("event");
		String uid=(String) session.getAttribute("username");
		event.setUserid(uid);
		eventDAO.saveOrUpdate(event);
		return new ResponseEntity<Event>(event ,HttpStatus.OK);
	}
	
	@DeleteMapping(value="/deleteevent/{eventid}")
	public ResponseEntity<Event> deleteevent(Event event,@PathVariable("eventid") int eventid){
		Event event1=eventDAO.get(eventid);
		eventDAO.delete(event1);
		return new ResponseEntity<Event>(event,HttpStatus.OK);
	}
	@GetMapping(value="/listevent")
	public ResponseEntity<List<Event>> listevent(){
		System.out.println("list of events");
		List<Event> event=eventDAO.list();
		return new ResponseEntity<List<Event>>(event,HttpStatus.OK);
	}
}
