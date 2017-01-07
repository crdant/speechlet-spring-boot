package io.crdant.spring.speechlet;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.json.SpeechletResponseEnvelope;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.speechlet.verifier.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class BootifulSpeechletRequestHandler {
    private final List<SpeechletRequestVerifier> requestVerifiers;
    private final List<SpeechletRequestEnvelopeVerifier> requestEnvelopeVerifiers;
    private final List<SpeechletResponseVerifier> responseVerifiers;

    public BootifulSpeechletRequestHandler(final List<SpeechletRequestVerifier> requestVerifiers,
                                   List<SpeechletResponseVerifier> responseVerifiers) {
        this(new ArrayList<SpeechletRequestEnvelopeVerifier>(1), requestVerifiers, responseVerifiers);
        addApplicationIdSpeechletRequestEnvelopeVerifierIfNecessary();
    }

    public BootifulSpeechletRequestHandler(final List<SpeechletRequestEnvelopeVerifier> requestEnvelopeVerifiers) {
        this(requestEnvelopeVerifiers, Collections.<SpeechletRequestVerifier>emptyList(), Arrays.asList(
                new ResponseSizeSpeechletResponseVerifier(),
                new OutputSpeechSpeechletResponseVerifier(), new CardSpeechletResponseVerifier()));
    }

    private BootifulSpeechletRequestHandler(
            final List<SpeechletRequestEnvelopeVerifier> requestEnvelopeVerifiers,
            final List<SpeechletRequestVerifier> requestVerifiers,
            final List<SpeechletResponseVerifier> responseVerifiers) {
        this.requestEnvelopeVerifiers = requestEnvelopeVerifiers;
        this.requestVerifiers = requestVerifiers;
        this.responseVerifiers = responseVerifiers;
    }

    /**
     * Processes the provided bytes as a request from the Alexa service and generates an appropriate
     * response after dispatching the request to the appropriate method calls on the
     * {@code SpeechletV2} provided at construction time.
     *
     * @param speechlet
     *            the speechlet to be invoked
     * @param serializedSpeechletRequest
     *            the request coming from Alexa service
     * @return the response that should be returned to the Alexa service. This comes from the
     *         appropriate method call in the configured {@code SpeechletV2}
     * @throws IOException
     *             may occur during request or response serialization
     * @throws SpeechletRequestHandlerException
     *             indicates a problem with the request or response
     * @throws SpeechletException
     *             indicates a problem from within the included {@code SpeechletV2}
     */
    public byte[] handleSpeechletCall(SpeechletV2 speechlet, byte[] serializedSpeechletRequest)
            throws IOException, SpeechletRequestHandlerException, SpeechletException {
        return internalHandleSpeechletCall(speechlet, serializedSpeechletRequest);
    }

    private byte[] internalHandleSpeechletCall(SpeechletV2 speechlet,
                                               byte[] serializedSpeechletRequest) throws IOException,
            SpeechletRequestHandlerException, SpeechletException {

        final SpeechletRequestEnvelope<?> requestEnvelope =
                SpeechletRequestEnvelope.fromJson(serializedSpeechletRequest);

        final SpeechletRequest request = requestEnvelope.getRequest();
        final Session session = requestEnvelope.getSession();

        // Verify request
        for (SpeechletRequestVerifier verifier : requestVerifiers) {
            if (!verifier.verify(request, session)) {
                String message =
                        String.format("Could not validate SpeechletRequest %s using verifier %s, "
                                + "rejecting request", request != null ? request.getRequestId()
                                : "null", verifier.getClass().getSimpleName());
                throw new SpeechletRequestHandlerException(message);
            }
        }

        for (SpeechletRequestEnvelopeVerifier verifier : requestEnvelopeVerifiers) {
            if (!verifier.verify(requestEnvelope)) {
                String message =
                        String.format("Could not validate SpeechletRequest %s using verifier %s, "
                                + "rejecting request", request != null ? request.getRequestId()
                                : "null", verifier.getClass().getSimpleName());
                throw new SpeechletRequestHandlerException(message);
            }
        }

        // Dispatch request to Speechlet
        SpeechletRequestDispatcher dispatcher = new SpeechletRequestDispatcher(speechlet);
        SpeechletResponseEnvelope responseEnvelope =
                dispatcher.dispatchSpeechletCall(requestEnvelope, session);

        // Verify response
        for (SpeechletResponseVerifier verifier : responseVerifiers) {
            if (!verifier.verify(responseEnvelope, session)) {
                String message =
                        String.format("Could not validate SpeechletResponse %s using verifier %s, "
                                + "rejecting response", request.getRequestId(), verifier
                                .getClass()
                                .getSimpleName());
                throw new SpeechletRequestHandlerException(message);
            }
        }

        return responseEnvelope.toJsonBytes();
    }

    private void addApplicationIdSpeechletRequestEnvelopeVerifierIfNecessary() {
        for (SpeechletRequestVerifier requestVerifier : requestVerifiers) {
            if (requestVerifier instanceof ApplicationIdSpeechletRequestVerifier) {
                Set<String> supportedApplicationIds =
                        ((ApplicationIdSpeechletRequestVerifier) requestVerifier)
                                .getSupportedApplicationIds();
                requestEnvelopeVerifiers.add(new ApplicationIdSpeechletRequestEnvelopeVerifier(
                        supportedApplicationIds));
            }
        }
    }
}
