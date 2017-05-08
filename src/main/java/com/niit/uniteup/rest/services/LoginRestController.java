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
import com.niit.uniteup.dao.UsersDAO;
import com.niit.uniteup.model.Friend;
import com.niit.uniteup.model.Users;

@RestController
public class LoginRestController {
	org.slf4j.Logger logger = LoggerFactory.getLogger(LoginRestController.class);
	
	@Autowired 
	UsersDAO usersDAO;
	@Autowired
	FriendDAO friendDAO;

	@GetMapping("/login/")
	public ResponseEntity<Users> login( @RequestHeader("username") String username,@RequestHeader("password") String password ,HttpSession session){
		System.err.println("Hello: "+username+" : "+password);
		Users users = usersDAO.authuser(username,password);
		if(users==null)
			{	
			logger.debug("Users Data: "+users);
			Users users1 = new Users();
			users1.errorCode = "404";
			logger.debug("Users Data set: "+users1.getErrorCode());

			return new ResponseEntity<Users>(users1,HttpStatus.OK);
				
					
	}else if(friendDAO.getfriendlist(username)==null){
		session.setAttribute("userLogged", users);
		session.setAttribute("uid", users.getId());
		session.setAttribute("username",users.getUsername());
		users.setStatus('o');
		usersDAO.saveOrUpdate(users);
		Users users1=usersDAO.oneuser(users.getId());
		users1.setErrorCode("200");
		return new ResponseEntity<Users>(users1,HttpStatus.OK);
	}else{
		session.setAttribute("userLogged", users);
		session.setAttribute("uid", users.getId());
		session.setAttribute("username",users.getUsername());
		 session.setAttribute("UserLoggedIn", "true");
		users.setStatus('o');
		usersDAO.saveOrUpdate(users);
    	List<Friend> friend=friendDAO.setonline(users.getUsername());
    	for(int i=0;i<friend.size();i++){
    		Friend online=friend.get(i);
    		online.setIsonline('y');
    		friendDAO.saveOrUpdate(online);
    	}
		Users users1=usersDAO.oneuser(users.getId());
		users1.setErrorCode("200");
		return new ResponseEntity<Users>(users1,HttpStatus.OK);
	}
	}
	@PostMapping("/logout")
	public ResponseEntity<Users> logout(HttpSession session){
		int uid =  (Integer) session.getAttribute("uid");
		System.err.println("LogOut function......!" + uid);
		
		Users users =usersDAO.oneuser(uid);
		users.setStatus('N');
		usersDAO.saveOrUpdate(users);
		List<Friend> friend=friendDAO.setonline(users.getUsername());
		for(int i=0;i<friend.size();i++){
    		Friend online=friend.get(i);
    		online.setIsonline('f');
    		friendDAO.saveOrUpdate(online);
    	}
		session.invalidate();
		return new ResponseEntity<Users>(users,HttpStatus.OK);
	}
}
