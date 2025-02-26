package es.iesjandula.ReaktorIssuesServer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService
{

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String text, String emailDocente) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom("jjurval099@g.educaand.es"); // Asegúrate de cambiar esto a tu correo
        message.setReplyTo(emailDocente);
        mailSender.send(message);
        
    }
    
    
}
