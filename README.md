# EmailTrigger
Trigger code to execute based on emails received

Program is written in Java and uses IMAP to connect to a gmail account. Program runs on a continuous loop, checking email from a gmail account on an interval delay (default is every 30 seconds). If an unread email from a select sender with the word "start" in the subject (no keywords are case sensitive) is retrieved, then code is triggered to run. Program continues running until an unread email from the same select sender with keyword "stop" in the subject is retrieved. If the user wants to test that the program is running without triggering any code, then send an email from select sender with "test" in the subject line. When an unread email from select sender that contains either "start", "stop" or "test" in the subject is retrieved, a confirmation email is sent out.

The inspiration for this project was to be able to execute code on a target machine without knowing in advance when the code would need to be executed. With EmailTrigger running on a target machine, a user can send emails from their phone (anywhere in the world, at any time of day) and have EmailTrigger execute code on target machine.

One dependency is needed for this project, javax.mail.jar. Security settings will need to be changed in gmail to allow EmailTrigger to connect to specified gmail account. If settings are not changed, a javax.mail.AuthenticationFailedException will be thrown. Instructions on how to change gmail settings can be found here: http://stackoverflow.com/a/33801654
