package com.niit.uniteup.rest.services;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.niit.uniteup.dao.FriendDAO;
import com.niit.uniteup.dao.UserDAO;
import com.niit.uniteup.model.Friend;
import com.niit.uniteup.model.User;

@RestController
public class LoginRestController {
	org.slf4j.Logger logger = LoggerFactory.getLogger(LoginRestController.class);
	
	@Autowired 
	UserDAO userDAO;
	@Autowired
	FriendDAO friendDAO;

	@GetMapping("/login/")
	public ResponseEntity<User> login( @RequestHeader("username") String username,@RequestHeader("password") String password ,HttpSession session){
		System.err.println("Hello: "+username+" : "+password);
		User user = userDAO.authuser(username,password);
		if(user==null)
			{	
			logger.debug("Users Data: "+user);
			User user1 = new User();
			user1.errorCode = "404";
			logger.debug("Users Data set: "+user1.getErrorCode());

			return new ResponseEntity<User>(user1,HttpStatus.OK);
				
					
	}else if(friendDAO.getfriendlist(username)==null){
		session.setAttribute("userLogged", user);
		session.setAttribute("uid", user.getId());
		session.setAttribute("username",user.getUsername());
		user.setStatus('o');
		userDAO.saveOrUpdate(user);
		User user1=userDAO.oneuser(user.getId());
		user1.setErrorCode("200");
		return new ResponseEntity<User>(user1,HttpStatus.OK);
	}else{
		session.setAttribute("userLogged", user);
		session.setAttribute("uid", user.getId());
		session.setAttribute("username",user.getUsername());
		 session.setAttribute("UserLoggedIn", "true");
		user.setStatus('o');
		userDAO.saveOrUpdate(user);
    	List<Friend> friend=friendDAO.setonline(user.getUsername());
    	for(int i=0;i<friend.size();i++){
    		Friend online=friend.get(i);
    		online.setIsonline('y');
    		friendDAO.saveOrUpdate(online);
    	}
		User user1=userDAO.oneuser(user.getId());
		user1.setErrorCode("200");
		return new ResponseEntity<User>(user1,HttpStatus.OK);
	}
	}
	@PostMapping("/logout")
	public ResponseEntity<User> logout(HttpSession session){
		int uid =  (Integer) session.getAttribute("uid");
		System.err.println("LogOut function......!" + uid);
		
		User user =userDAO.oneuser(uid);
		user.setStatus('N');
		userDAO.saveOrUpdate(user);
		List<Friend> friend=friendDAO.setonline(user.getUsername());
		for(int i=0;i<friend.size();i++){
    		Friend online=friend.get(i);
    		online.setIsonline('f');
    		friendDAO.saveOrUpdate(online);
    	}
		session.invalidate();
		return new ResponseEntity<User>(user,HttpStatus.OK);
	}
}
