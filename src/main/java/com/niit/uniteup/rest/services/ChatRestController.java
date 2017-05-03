package com.niit.uniteup.rest.services;

import java.util.Date;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.niit.uniteup.model.Message;
import com.niit.uniteup.model.OutputMessage;

@Controller
public class ChatRestController {

	@MessageMapping("/chat")
	@SendTo("/topic/message")
	public OutputMessage sendMessage(Message message){
		return new OutputMessage(message,new Date());
	}
	}
