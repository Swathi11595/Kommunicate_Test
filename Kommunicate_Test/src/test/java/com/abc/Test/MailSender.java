package com.abc.Test;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.MultiPartEmail;

public class MailSender 
{
	public static void mail(MultiPartEmail em) throws Exception
	{
		em.setHostName("smtp.gmail.com");
		em.setSmtpPort(465);
		em.setAuthenticator(new DefaultAuthenticator("username@gmail.com","password"));
		em.setSSLOnConnect(true);
		em.setFrom("username@gmail.com");
	}
	
	public static void attachment(EmailAttachment at, String f)
	{
		at.setPath(f);
		at.setDisposition(EmailAttachment.ATTACHMENT);
		at.setDescription("Report");
	}
}
