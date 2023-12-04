package com.callthirdpartyapi.service;

import java.util.Date;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.callthirdpartyapi.dto.ClientRequest;
import com.callthirdpartyapi.model.Client;
import com.callthirdpartyapi.repo.ClientRepository;

import jakarta.mail.internet.MimeMessage;

@Service
public class RegisterService {
	
	@Autowired
	private ClientRepository clientRepo;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${account.lock.time}")
    private long LOCK_DURATION_TIME;
    
	
	public String registerclient(ClientRequest client , String url) throws Exception {
		
		client.setPassword(
				passwordEncoder.encode(client.getPassword())
				);
		
		Client c = mapper.map(client, Client.class);
		c.setId((long) 0);
		c.setEnable(false);
		c.setVerificationCode(UUID.randomUUID().toString());
		c.setRole("ROLE_USER");
		clientRepo.save(c);
			
		sendMail(c,url);
		
		return "Register successfully";
		
	}
	
	@Value("${spring.mail.username}")
	private String from;
	
	public void sendMail(Client client , String url) throws Exception {
		
		String toAddress = client.getUsername();
	    String senderName = "noreply@nimap.com";
	    String subject = "Please verify your registration";
	    String content = "Dear [[name]],<br>"
	            + "Please click the link below to verify your registration:<br>"
	            + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
	            + "Thank you,<br>"
	            + "Your company name.";
	     
	    MimeMessage message = mailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(message);
	 
	    helper.setFrom(from, senderName);
	    helper.setTo(toAddress);
	    helper.setSubject(subject);
	     
	    content = content.replace("[[name]]", client.getName());
	    String verifyURL = url + "/verify?code=" + client.getVerificationCode();
	     
	    content = content.replace("[[URL]]", verifyURL);
	     
	    helper.setText(content, true); 
	     
	    mailSender.send(message);
	    System.out.println("mail sended");
	}
	
	public boolean verify(String verifyCode) {
		Client c = clientRepo.findByVerificationCode(verifyCode);
		if(c != null) {
		c.setEnable(true);
		c.setVerificationCode(null);
		c.setFailedAttempt((short)0);
		c.setIsAccountNonLock(true);
		clientRepo.save(c);
		return true;
		}
		
		return false;
	}
	
	public void increaseFailAttempt(Client client) {
		
		clientRepo.updateFailedAttempt(
					(short) (client.getFailedAttempt()+1) ,
					client.getUsername()
					);
		
	}
	
	public void resetAttempt(String email) {
		clientRepo.updateFailedAttempt(Short.valueOf("0"), email);
	}
	
	public void lock(Client client) {
		
		client.setIsAccountNonLock(false);
		client.setLockTime(new Date());
		clientRepo.save(client);
		
	}
	
	
	public boolean unlockAccountTimeExpired(Client client) {
		
		long clientLockTime = client.getLockTime().getTime();
		long currentTime = System.currentTimeMillis();
		
		if(clientLockTime + LOCK_DURATION_TIME < currentTime) {
			
			client.setIsAccountNonLock(true);
			client.setLockTime(null);
			client.setFailedAttempt(Short.valueOf("0"));
			clientRepo.save(client);
			
			return true;
		}
		
		return false;
	}
	
	
	

}
