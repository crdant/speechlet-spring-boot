package io.crdant.spring.speechlet.web.servlet;

import com.amazon.speech.Sdk;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletRequestHandler;
import com.amazon.speech.speechlet.SpeechletRequestHandlerException;
import com.amazon.speech.speechlet.SpeechletV2;
import com.amazon.speech.speechlet.authentication.SpeechletRequestSignatureVerifier;
import com.amazon.speech.speechlet.servlet.SpeechletServlet;
import io.crdant.spring.speechlet.web.SpeechletServletRequest;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Component
public class BootifulSpeechletServlet extends SpeechletServlet {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SpeechletRequestHandler speechletRequestHandler;

    protected SpeechletV2 speechlet ;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SpeechletServletRequest speechletServletRequest = new SpeechletServletRequest(request);

        byte[] outputBytes = null ;
        try {
            outputBytes =
                    speechletRequestHandler.handleSpeechletCall(speechlet,
                            speechletServletRequest.getSerializedRequest());
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
        super.doPost(request, response);
    }
}
