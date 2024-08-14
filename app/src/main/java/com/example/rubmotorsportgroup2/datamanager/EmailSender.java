package com.example.rubmotorsportgroup2.datamanager;

import static com.example.rubmotorsportgroup2.MainActivity.sm;
import static java.util.Arrays.asList;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class EmailSender {

    private final String stringSenderEmail = "senderderemail@gmail.com";     //google Mail
    private final String stringPasswordSenderEmail = "xoishtofubhmydni";     // AppPassword for the Mail
    private final String stringHost = "smtp.gmail.com";

    private List<String> stringReceiverEmail = sm.getMails();                //Mails saved in settings

    public static boolean isValidEmail(CharSequence target) {                // checker if a Email is a actual Email
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public void SendErrorMail(ArrayList<String> ErrorCase) {

        for (int i = 0; i < stringReceiverEmail.size(); i++)
            try {
                if(isValidEmail(stringReceiverEmail.get(i))){
                    String currEmail = stringReceiverEmail.get(i);
                    Properties properties = System.getProperties();

                    properties.put("mail.smtp.host", stringHost);
                    properties.put("mail.smtp.port", "465");
                    properties.put("mail.smtp.ssl.enable", "true");
                    properties.put("mail.smtp.auth", "true");


                    javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);
                        }
                    });
                    MimeMessage mimeMessage = new MimeMessage(session);
                    mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(currEmail));


                    mimeMessage.setSubject("Subject: Error Case! ");
                    StringBuilder textnachricht = new StringBuilder("Hello User " + (i + 1) + "\n Error found! \n\n");

                    for (int j = 0; j < ErrorCase.size(); j++) {
                        textnachricht.append(ErrorCase.get(j) + "\n\n");
                    }

                    mimeMessage.setText(textnachricht.toString());

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Transport.send(mimeMessage);
                            } catch (MessagingException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();
                };
            }catch (AddressException e) {
                e.printStackTrace();
            } catch (MessagingException e) {
                e.printStackTrace();

            }
    }

    public void SendCustomEmail(String customHeader,String customText){

        for (int i = 0; i < stringReceiverEmail.size(); i++)
            try {
                if(isValidEmail(stringReceiverEmail.get(i))){
                    String currEmail = stringReceiverEmail.get(i);

                    Properties properties = System.getProperties();

                    properties.put("mail.smtp.host", stringHost);
                    properties.put("mail.smtp.port", "465");
                    properties.put("mail.smtp.ssl.enable", "true");
                    properties.put("mail.smtp.auth", "true");


                    javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);
                        }
                    });
                    MimeMessage mimeMessage = new MimeMessage(session);
                    mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(currEmail));


                    mimeMessage.setSubject(customHeader);
                    mimeMessage.setText(customText);

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Transport.send(mimeMessage);
                            } catch (MessagingException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();
                };
            } catch (AddressException e) {
                e.printStackTrace();
            } catch (MessagingException e) {
                e.printStackTrace();

            }
    }

}
