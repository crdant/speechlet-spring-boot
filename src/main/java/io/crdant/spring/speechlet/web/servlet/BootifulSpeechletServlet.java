package io.crdant.spring.speechlet.web.servlet;

import com.amazon.speech.json.SpeechletResponseEnvelope;
import com.amazon.speech.speechlet.SpeechletRequestDispatcher;
import com.amazon.speech.speechlet.SpeechletRequestHandler;
import com.amazon.speech.speechlet.SpeechletRequestHandlerException;
import com.amazon.speech.speechlet.SpeechletV2;
import io.crdant.spring.speechlet.SpeechletMapping;
import io.crdant.spring.speechlet.web.SpeechletServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Component
public class BootifulSpeechletServlet extends HttpServlet {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SpeechletMapping mapping ;

    protected SpeechletV2 speechlet ;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SpeechletServletRequest speechletServletRequest = null ;

        try {
            speechletServletRequest = new SpeechletServletRequest(request);
        } catch ( IllegalArgumentException ex ) {
            int statusCode = HttpServletResponse.SC_BAD_REQUEST;
            response.sendError(statusCode, ex.getMessage());
            return;
        }

        SpeechletV2 speechlet = lookupSpeechlet(speechletServletRequest);

        byte[] outputBytes = null ;
        try {
            // Dispatch request to Speechlet
            SpeechletRequestDispatcher dispatcher = new SpeechletRequestDispatcher(speechlet);
            SpeechletResponseEnvelope responseEnvelope =
                    dispatcher.dispatchSpeechletCall(speechletServletRequest.getRequestEnvelope(), speechletServletRequest.getSpeechletSession());
            outputBytes = responseEnvelope.toJsonBytes();
        } catch (SpeechletRequestHandlerException | SecurityException ex) {
            int statusCode = HttpServletResponse.SC_BAD_REQUEST;
            logger.error("Exception occurred in doPost, returning status code {}", statusCode, ex);
            response.sendError(statusCode, ex.getMessage());
            return;
        } catch (Exception ex) {
            int statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            logger.error("Exception occurred in doPost, returning status code {}", statusCode, ex);
            response.sendError(statusCode, ex.getMessage());
            return;
        }

        // Generate JSON and send back the response
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        try (final OutputStream out = response.getOutputStream()) {
            response.setContentLength(outputBytes.length);
            out.write(outputBytes);
        }
        logger.debug("About to call the superclass, you should have seen some output by now");
        super.doPost(request, response);
    }

    /**
     * Load a Speechlet for the given applicationId. Speechlets that are annoted with the
     * Speechlet annotation will be located and tested to see if they are mapped to the same
     * given application.
     *
     * @param request
     * @return
     */
    protected SpeechletV2 lookupSpeechlet (SpeechletServletRequest request ) {
        return mapping.lookupSpeechlet(request.getApplicationId());
    }

}
