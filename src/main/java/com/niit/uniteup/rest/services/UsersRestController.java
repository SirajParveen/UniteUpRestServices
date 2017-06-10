package com.niit.uniteup.rest.services;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.niit.uniteup.dao.UsersDAO;
import com.niit.uniteup.model.Users;

@RestController
public class UsersRestController {

	@Autowired
	private UsersDAO usersDAO;

	@PostMapping(value = "/register")
	public ResponseEntity<Users> adduser(@RequestBody Users users) {
		System.out.println("hello");
		users.setStatus('n');
		users.setIsonline('N');
		usersDAO.saveOrUpdate(users);
		return new ResponseEntity<Users>(users, HttpStatus.OK);
	}

	@GetMapping(value = "/users")
	public ResponseEntity<List<Users>> listuser() {
		System.out.println("list of users");
		List<Users> users1 = usersDAO.list();
	
		return new ResponseEntity<List<Users>>(users1, HttpStatus.OK);
	}

	@GetMapping(value = "/oneuser")
	public ResponseEntity<Users> oneuser(HttpSession session) {
		String username = (String) session.getAttribute("username");
		Users oneuser = usersDAO.profileof(username);
		return new ResponseEntity<Users>(oneuser, HttpStatus.OK);
	}

	@GetMapping("/profileimage")
	public ResponseEntity<Users> profileimage(HttpSession session) {
		int uid = (Integer) session.getAttribute("uid");
		Users users = usersDAO.oneuser(uid);
		return new ResponseEntity<Users>(users, HttpStatus.OK);
	}

	@GetMapping("/nonfriends")
	public ResponseEntity<List<Users>> nonfriends(HttpSession session) {
		int uid = (Integer) session.getAttribute("uid");
		String username = (String) session.getAttribute("username");
		List<Users> nonfriends = usersDAO.nonfriends(uid,username);
		return new ResponseEntity<List<Users>>(nonfriends, HttpStatus.OK);
	}

	@GetMapping("/hello")
	public String sayhello()
	{
		return ("hello i am siraj");
	}
}
