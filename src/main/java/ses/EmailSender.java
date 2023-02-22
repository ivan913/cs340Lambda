package ses;

// these are the imports for SDK v1
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.simpleemail.*;
import com.amazonaws.services.simpleemail.model.*;
import com.amazonaws.regions.Regions;
import org.joda.time.DateTime;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class EmailSender {
    public EmailResult handleRequest(EmailRequest request, Context context) {

        LambdaLogger logger = context.getLogger();
        logger.log("Entering send_email");
        EmailResult result = new EmailResult();
        try {
            AmazonSimpleEmailService client =
                    AmazonSimpleEmailServiceClientBuilder.standard()

                            // Replace US_WEST_2 with the AWS Region you're using for
                            // Amazon SES.
                            .withRegion(Regions.US_WEST_2).build();

            // TODO:
            // Use the AmazonSimpleEmailService object to send an email message
            // using the values in the EmailRequest parameter object
            ArrayList<String> list = new ArrayList<>();
            list.add(request.to);
            Destination destination = new Destination(list);
            Body body = new Body();
            body.setHtml(new Content(request.htmlBody));
            body.setText(new Content(request.textBody));
            Message message = new Message(new Content(request.subject),body);
            SendEmailRequest sendEmailRequest = new SendEmailRequest(request.from, destination, message);
            client.sendEmail(sendEmailRequest);
            logger.log("Email sent!");
            result.message = "It worked";
            result.timestamp = new DateTime(DateTime.now()).toString();
        } catch (Exception ex) {
            logger.log("The email was not sent. Error message: " + ex.getMessage());
            result.message = "It worked";
            result.timestamp = new DateTime(DateTime.now()).toString();
            throw new RuntimeException(ex);
        }
        finally {
            logger.log("Leaving send_email");
        }

        // TODO:
        // Return EmailResult
        return result;
    }

}