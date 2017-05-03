package com.niit.uniteup.rest.services;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.niit.uniteup.dao.JobDAO;
import com.niit.uniteup.model.Job;

@RestController
public class JobRestController {

	@Autowired
	private JobDAO jobDAO;
	
	@PostMapping(value="/createjob")
	public ResponseEntity<Job> createjob(@RequestBody Job job,HttpSession session){
		int uid=(Integer) session.getAttribute("uid");
		job.setUserid(uid);
		job.setDoc(new Date());
		jobDAO.saveOrUpdate(job);
		return new ResponseEntity<Job>(job,HttpStatus.OK);
	}
	@GetMapping(value="/getjobs")
	public ResponseEntity<List<Job>> getjobs(){
		List<Job> jobs =jobDAO.list();
		return new ResponseEntity<List<Job>>(jobs,HttpStatus.OK);
	}
}
