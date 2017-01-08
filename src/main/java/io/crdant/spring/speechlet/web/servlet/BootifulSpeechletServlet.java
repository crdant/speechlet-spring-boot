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
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Component
public class BootifulSpeechletServlet extends HttpServlet {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SpeechletMapping mapping ;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // this is assured by the servlet filter before we get here
        SpeechletServletRequest speechletServletRequest = (SpeechletServletRequest) request ;
        List<SpeechletV2> speechlets = lookupSpeechlet(speechletServletRequest);
        logger.debug("speechlets: " + speechlets);

        byte[] outputBytes = null ;
        try {
            for ( SpeechletV2 speechlet : speechlets ) {
                SpeechletRequestDispatcher dispatcher = new SpeechletRequestDispatcher(speechlet);
                SpeechletResponseEnvelope responseEnvelope =
                        dispatcher.dispatchSpeechletCall(speechletServletRequest.getRequestEnvelope(), speechletServletRequest.getSpeechletSession());
                logger.debug("response: " + responseEnvelope.getResponse());
                if ( responseEnvelope != null && responseEnvelope.getResponse() != null ) {
                    outputBytes = responseEnvelope.toJsonBytes();
                    break ;
                }
            }
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
    }

    /**
     * Load a Speechlet for the given applicationId. Speechlets that are annoted with the
     * Speechlet annotation will be located and tested to see if they are mapped to the same
     * given application.
     *
     * @param request
     * @return
     */
    protected List<SpeechletV2> lookupSpeechlet (SpeechletServletRequest request ) {
        List<SpeechletV2> speechlets = mapping.lookupSpeechlet(request.getApplicationId());
        return speechlets;
    }

}
