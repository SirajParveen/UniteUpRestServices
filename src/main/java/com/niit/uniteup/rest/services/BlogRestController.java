package com.niit.uniteup.rest.services;

import java.util.Date;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.niit.uniteup.dao.BlogDAO;
import com.niit.uniteup.dao.BlogLikesDAO;
import com.niit.uniteup.model.Blog;
import com.niit.uniteup.model.BlogLikes;

@RestController
public class BlogRestController {
	@Autowired
	private BlogDAO blogDAO;

	@Autowired
	private BlogLikesDAO blogLikesDAO;

	@PostMapping(value = "/createblog")
	public ResponseEntity<Blog> addblog(@RequestBody Blog blog, HttpSession session) {
		System.out.println("AddBlog Function Stared...........................!");
		String name = blog.getTitle();
		System.out.println(name);
		int uid = (Integer) session.getAttribute("uid");
		blog.setStatus("n");
		blog.setDoc(new Date());
		blog.setUserid(uid);
		blogDAO.saveOrUpdate(blog);
		return new ResponseEntity<Blog>(blog, HttpStatus.OK);

	}

	@GetMapping(value = "/blog")
	public ResponseEntity<List<Blog>> listblog() {
		System.out.println("list of blog");
		List<Blog> blog = blogDAO.userlist();
		return new ResponseEntity<List<Blog>>(blog, HttpStatus.OK);
	}

	@GetMapping(value = "/adminblog")
	public ResponseEntity<List<Blog>> adminblog() {
		System.out.println("list of blog");
		List<Blog> blog = blogDAO.list();
		return new ResponseEntity<List<Blog>>(blog, HttpStatus.OK);
	}

	@DeleteMapping(value = "/deleteblog/{blogid}")
	public ResponseEntity<Blog> deleteblog(Blog blog, @PathVariable("blogid") int blogid) {
		System.err.println("blogid"+blogid);
		Blog blog1 = blogDAO.get(blogid);
		blogDAO.delete(blog1);
		return new ResponseEntity<Blog>(blog, HttpStatus.OK);
	}
	
	@GetMapping(value = "/bloglikes")
	public ResponseEntity<List<BlogLikes>> listbloglikes( HttpSession session) {
		int uid = (Integer) session.getAttribute("uid");
		System.out.println("list of bloglikes");
		List<BlogLikes> blog = blogLikesDAO.list(uid);
		return new ResponseEntity<List<BlogLikes>>(blog, HttpStatus.OK);
	}

	@PostMapping(value = "/likeblog/{blogid}")
	public ResponseEntity<Blog> likeblog(BlogLikes blogLikes, @PathVariable("blogid") int blogid, HttpSession session) {
		int uid = (Integer) session.getAttribute("uid");
		System.err.println("uid is="+session.getAttribute("uid"));
		blogLikes.setBlogid(blogid);
		blogLikes.setUserid(uid);
		System.err.println("user Id: "+uid);
		blogLikes.setLikes("like");
		blogLikesDAO.saveOrUpdate(blogLikes);
		List<BlogLikes> list = blogLikesDAO.bloglist(blogid);
		Blog blog = blogDAO.get(blogid);
		blog.setBloglike(list.size());
		blogDAO.saveOrUpdate(blog);
		return new ResponseEntity<Blog>(HttpStatus.OK);
	}

	@DeleteMapping(value = "/unlikeblog/{blogid}")
	public ResponseEntity<Blog> unlike(@PathVariable("blogid") int blogid, HttpSession session) {
		int uid = (Integer) session.getAttribute("uid");
		BlogLikes blogLikes = blogLikesDAO.list(uid, blogid);
		blogLikesDAO.delete(blogLikes);
		List<BlogLikes> list = blogLikesDAO.bloglist(blogid);
		Blog blog = blogDAO.get(blogid); 
		blog.setBloglike(list.size());
		blogDAO.saveOrUpdate(blog);
		return new ResponseEntity<Blog>(HttpStatus.OK);
	}

	@PostMapping(value = "/acceptblog/{id}")
	public ResponseEntity<Blog> accept(@PathVariable("id") int id) {
		Blog blog = blogDAO.get(id);
		blog.setStatus("a");
		blogDAO.saveOrUpdate(blog);
		return new ResponseEntity<Blog>(HttpStatus.OK);
	}

	@PostMapping(value = "/rejectblog/{id}")
	public ResponseEntity<Blog> reject(@PathVariable("id") int id) {
		Blog blog = blogDAO.get(id);
		blog.setStatus("r");
		blogDAO.saveOrUpdate(blog);
		return new ResponseEntity<Blog>(HttpStatus.OK);
	}
	
	@GetMapping(value = "/editblog/{blogid}/{title}/{content}")
	public ResponseEntity<Blog> editblog(Blog blog, @PathVariable("blogid") int blogid, @PathVariable("title") String title, @PathVariable("content") String content, RedirectAttributes attributes) {
		System.err.println("blogid"+blogid+title+content);
	     attributes.addFlashAttribute("category", this.blogDAO.get(blogid));
		return new ResponseEntity<Blog>(blog, HttpStatus.OK);
	}
}
