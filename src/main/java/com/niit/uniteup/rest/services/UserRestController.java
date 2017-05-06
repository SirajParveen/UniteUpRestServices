package com.niit.uniteup.rest.services;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.niit.uniteup.dao.UserDAO;
import com.niit.uniteup.model.User;

@RestController
public class UserRestController {

	@Autowired
	private UserDAO userDAO;
	
	/*@Autowired
	private User user;*/
	
	@PostMapping(value = "/register")
	public ResponseEntity<User> adduser(@RequestBody User user) {
		System.out.println("Registering the users");
		user.setStatus('n');
		user.setIsonline('N');
		userDAO.saveOrUpdate(user);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	@GetMapping(value = "/users")
	public ResponseEntity<List<User>> listuser() {
		System.out.println("list of User");
		List<User> User1 = userDAO.list();
	
		return new ResponseEntity<List<User>>(User1, HttpStatus.OK);
	}

	@GetMapping(value = "/oneuser")
	public ResponseEntity<User> oneuser(HttpSession session) {
		String username = (String) session.getAttribute("username");
		User oneuser = userDAO.profileof(username);
		return new ResponseEntity<User>(oneuser, HttpStatus.OK);
	}
	
	@PostMapping("/imageUpload")
	public void ImageUpload(@RequestBody MultipartFile file, HttpSession session) throws IOException {

		String username = (String) session
				.getAttribute("username"); // Get Logged in Username 
		User User = userDAO
				.profileof(username); // Get user object based on username 
		System.out.println(file.getContentType() + '\n' + file.getName() + '\n' + file.getSize() + '\n'
				+ file.getOriginalFilename());
		User.setImage(file.getBytes());
		userDAO.saveOrUpdate(User);
	}

	@GetMapping("/profileimage")
	public ResponseEntity<User> profileimage(HttpSession session) {
		int uid = (Integer) session.getAttribute("uid");
		User User = userDAO.oneuser(uid);
		return new ResponseEntity<User>(User, HttpStatus.OK);
	}

	@GetMapping("/nonfriends")
	public ResponseEntity<List<User>> nonfriends(HttpSession session) {
		int uid = (Integer) session.getAttribute("uid");
		List<User> nonfriends = userDAO.nonfriends(uid);
		return new ResponseEntity<List<User>>(nonfriends, HttpStatus.OK);
	}

	@GetMapping("/hello")
	public  String sayhello()
	{
		return ("hello i am siraj");
	}
}
