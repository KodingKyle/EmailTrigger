import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.Flags;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;




public class EmailTrigger {
	public static void main(String arg[]){
		EmailTrigger et = new EmailTrigger();
		et.run();
	}
   
    public void run() {
	    //***************************************************************************************************************
	    //Change variables below as desired
	    //***************************************************************************************************************
	    //address to check for trigger email
        String user = "user@gmail.com";
        //password for address to check for trigger email
        String password = "password!";
        //address that trigger email will be sent from
        String sendingEmail = "sendingEmail@gmail.com";
        //address to send confirmation email to that triggered code was executed
        String confirmEmail = "confirmEmail@gmail.com";
        String confirmSubject = "Email Trigger";
        String confirmBody = "Received email with subject";
        //how long to pause before checking email again (in ms)
        int delayEmailCheck = 30000;
        //***************************************************************************************************************
        read(user, password, sendingEmail, confirmEmail, confirmSubject, confirmBody, delayEmailCheck);
    }

    public void read(String user, String password, String sendingEmail, String confirmEmail, String confirmSubject, String confirmBody, int delayEmailCheck) {
        Properties props = new Properties();
        Session session;
        Store store;
        Folder inbox;
        int messageCount;
        Message[] messages;
        String fromAddress;
        String subject;
        MimeMessage confimationMessage;
        boolean done = false;

        try {

            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.port", "465");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            session = Session.getDefaultInstance(props, null);
            

            store = session.getStore("imaps");
            store.connect("smtp.gmail.com", user, password);

            while(!done){
	            confimationMessage = new MimeMessage(session);
	            inbox = store.getFolder("inbox");
	            inbox.open(Folder.READ_WRITE);
	            messageCount = inbox.getMessageCount();
	
	            System.out.println("Total Messages:- " + messageCount);
	
	            messages = inbox.getMessages();
	            System.out.println("------------------------------");
	            for (int i = 0; i < messageCount; i++) {
		            subject = messages[i].getSubject();
		            fromAddress = InternetAddress.toString(messages[i].getFrom());
	                System.out.println("Message: " + i);
	                System.out.println("Mail Subject:- " + subject);
	                System.out.println("Mail From:- " + fromAddress);
	                
	
	                if (!(messages[i].isSet(Flags.Flag.SEEN)) && fromAddress.contains(sendingEmail) && (subject.toLowerCase().contains("start") || subject.toLowerCase().contains("test") || subject.toLowerCase().contains("stop"))) {
		                //mark email as read
	                    System.out.println("message marked read");
	                    messages[i].setFlag(Flags.Flag.SEEN, true);
	                    
	                    //***************************************************************************************************************
	                    //Enter code here to be triggered
	                    //***************************************************************************************************************
	                    if(subject.toLowerCase().contains("start")){
	                    	System.out.println("Running code from email trigger");
                    	}
	                    //***************************************************************************************************************
	                    
	                    //if subject contained the word "stop", then exit program
	                    if(subject.toLowerCase().contains("stop")){
	                    	done = true;
                    	}
	                    
	                    InternetAddress from = new InternetAddress(user);
	                    confimationMessage.setSubject(confirmSubject);
	                    confimationMessage.setFrom(from);
	                    confimationMessage.addRecipients(Message.RecipientType.TO, InternetAddress.parse(confirmEmail));
	
	                    // Create a multi-part to combine the parts
	                    Multipart multipart = new MimeMultipart("alternative");
	
	                    // Create your text message part
	                    BodyPart messageBodyPart = new MimeBodyPart();
	                    messageBodyPart.setText(confirmBody + " " + subject);
	
	                    // Add the text part to the multipart
	                    multipart.addBodyPart(messageBodyPart);
	
	                    // Associate multi-part with message
	                    confimationMessage.setContent(multipart);
	
	                    // Send message
	                    Transport transport = session.getTransport("smtp");
	                    transport.connect("smtp.gmail.com", user, password);
	                    //System.out.println("Transport: " + transport.toString());
	                    transport.sendMessage(confimationMessage, confimationMessage.getAllRecipients());
	
	
	                }
	
	            }
	            System.out.println("------------------------------");
	            System.out.println();
	            inbox.close(true);
	            
	            //pause checking of inbox
	            Thread.sleep(delayEmailCheck);
            }
            store.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}