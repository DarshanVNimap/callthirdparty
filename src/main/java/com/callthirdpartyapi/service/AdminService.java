package com.callthirdpartyapi.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.callthirdpartyapi.dto.ClientRequest;
import com.callthirdpartyapi.model.Client;
import com.callthirdpartyapi.repo.ClientRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class AdminService {
	
	@Autowired
	private ClientRepository clientRepo;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
    @Autowired
    private JavaMailSender mailSender;
	
	public List<ClientRequest> getclients() {
		
		return clientRepo.findAll().stream()
							.map( client ->  mapper.map(client ,ClientRequest.class))
							.collect(Collectors.toList());
		
	}
	
	public ClientRequest getclient(Long id) {
		Client c = clientRepo.findById(id).orElseThrow();
		ClientRequest cr = mapper.map(c, ClientRequest.class);
		cr.setPassword("Confidencial");
		return cr;
		
	}
	
	public String registerclient(ClientRequest client) {
		
		client.setPassword(
				passwordEncoder.encode(client.getPassword())
				);
		
		Client c = mapper.map(client, Client.class);
		c.setId((long) 0);
		c.setEnable(false);
		c.setVerificationCode(UUID.randomUUID().toString());
		clientRepo.save(c);
		
		return "Register successfully";
		
	}
	
	
	public void sendMail(Client client , String url) throws Exception {
		String toAddress = client.getUsername();
	    String fromAddress = "valadarshan2002@gmail.com";
	    String senderName = "noreply@nimap.com";
	    String subject = "Please verify your registration";
	    String content = "Dear [[name]],<br>"
	            + "Please click the link below to verify your registration:<br>"
	            + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
	            + "Thank you,<br>"
	            + "Your company name.";
	     
	    MimeMessage message = mailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(message);
	     
	    helper.setFrom(fromAddress, senderName);
	    helper.setTo(toAddress);
	    helper.setSubject(subject);
	     
	    content = content.replace("[[name]]", client.getName());
	    String verifyURL = url + "/verify?code=" + client.getVerificationCode();
	     
	    content = content.replace("[[URL]]", verifyURL);
	     
	    helper.setText(content, true); 
	     
	    mailSender.send(message);
	}
	
	
	
	

}
