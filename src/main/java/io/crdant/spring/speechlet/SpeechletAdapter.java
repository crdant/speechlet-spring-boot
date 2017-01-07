package io.crdant.spring.speechlet;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.*;
import com.amazon.speech.speechlet.*;
import io.crdant.spring.speechlet.annotation.*;
import io.crdant.spring.speechlet.annotation.Slot;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SpeechletAdapter implements SpeechletV2 {

    final Object speechletBean ;
    private Method launchMethod ;
    private Method sessionStartedMethod ;
    private Method sessionEndedMethod ;
    private final Map<String,Method> intentMapping ;

    public SpeechletAdapter(Object speechletBean) {
        this.speechletBean = speechletBean ;
        this.intentMapping = new LinkedHashMap<String,Method>();

        findLaunchMethod(speechletBean);
        findSessionStartedMethod(speechletBean);
        mapIntents(speechletBean);
        findSessionEndedMethod(speechletBean);

    }

    private void mapIntents(Object speechletBean) {
        List<Method> methods = Arrays.asList(speechletBean.getClass().getMethods());
        for ( Method method : methods ) {
            if (method.getName().equals("onLaunch")) {
                intentMapping.put("*", method);
                break;
            } else {
                IntentMapping intent = AnnotationUtils.findAnnotation(method, IntentMapping.class);
                if (intent != null) {
                    intentMapping.put(intent.value()[0], method);
                }
            }
        }
    }

    private void findLaunchMethod(Object speechletBean) {
        List<Method> methods = Arrays.asList(speechletBean.getClass().getMethods());
        for ( Method method : methods ) {
            if (method.getName().equals("onLaunch")) {
                launchMethod = method;
                break;
            } else {
                Launch launch = AnnotationUtils.findAnnotation(method, Launch.class);
                if (launch != null) launchMethod = method;
            }
        }
    }

    private void findSessionStartedMethod(Object speechletBean) {
        List<Method> methods = Arrays.asList(speechletBean.getClass().getMethods());
        for ( Method method : methods ) {
            if (method.getName().equals("onSessionStarted")) {
                sessionStartedMethod = method;
                break;
            } else {
                SessionStarted started = AnnotationUtils.findAnnotation(method, SessionStarted.class);
                if (started != null) sessionStartedMethod = method;
            }
        }
    }

    private void findSessionEndedMethod(Object speechletBean) {
        List<Method> methods = Arrays.asList(speechletBean.getClass().getMethods());
        for ( Method method : methods ) {
            if ( method.getName().equals("onSessionEnded") ) {
                sessionEndedMethod = method ;
                break ;
            } else {
                SessionEnded ended = AnnotationUtils.findAnnotation(method, SessionEnded.class);
                if (ended != null) sessionEndedMethod = method;
            }
        }
    }

    @Override
    public SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> requestEnvelope) {
        Session session = requestEnvelope.getSession();
        Context context = requestEnvelope.getContext();
        LaunchRequest request = requestEnvelope.getRequest();

        try {
            Class<?> parameterTypes[] = launchMethod.getParameterTypes();
            if (parameterTypes.length > 1 && parameterTypes[0].equals(SessionStartedRequest.class) && parameterTypes[1].equals(Session.class)) {
                return (SpeechletResponse) launchMethod.invoke(requestEnvelope.getRequest(), requestEnvelope.getSession());
            } else {
                return (SpeechletResponse) launchMethod.invoke(speechletBean, requestEnvelope);
            }
        } catch ( IllegalAccessException accessEx ) {
            throw new RuntimeException("Non-public method was annotated as a handler for this request", accessEx);
        } catch ( InvocationTargetException targetEx ) {
            throw new RuntimeException("Invoked method through an unhandled exception", targetEx );
        }
    }

    @Override
    public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope) {
        Session session = requestEnvelope.getSession();
        Context context = requestEnvelope.getContext();
        SessionStartedRequest request = requestEnvelope.getRequest();

        try {
            Class<?> parameterTypes[] = sessionStartedMethod.getParameterTypes();
            if (parameterTypes.length > 1 && parameterTypes[0].equals(SessionStartedRequest.class) && parameterTypes[1].equals(Session.class)) {
                sessionStartedMethod.invoke(requestEnvelope.getRequest(), requestEnvelope.getSession());
            } else {
                sessionStartedMethod.invoke(speechletBean, requestEnvelope);
            }
        } catch ( IllegalAccessException accessEx ) {
            throw new RuntimeException("Non-public method was annotated as a handler for this request", accessEx);
        } catch ( InvocationTargetException targetEx ) {
            throw new RuntimeException("Invoked method through an unhandled exception", targetEx );
        }

    }

    @Override
    public SpeechletResponse onIntent(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        Session session = requestEnvelope.getSession();
        Context context = requestEnvelope.getContext();
        IntentRequest request = requestEnvelope.getRequest();

        Intent intent = request.getIntent() ;
        Method intentMethod = null ;
        if ( intentMapping.get("*") != null ) {
            intentMethod = intentMapping.get("*");
        } else {
            intentMethod = intentMapping.get(intent.getName());
        }
        Class<?> parameterTypes[] = sessionEndedMethod.getParameterTypes() ;

        try {
            if (parameterTypes.length > 1 && parameterTypes[0].equals(SessionStartedRequest.class) && parameterTypes[1].equals(Session.class)) {
                return (SpeechletResponse) intentMethod.invoke(requestEnvelope.getRequest(), requestEnvelope.getSession());
            } else if (parameterTypes.length == 1 && parameterTypes[0].equals(SpeechletRequestEnvelope.class)) {
                return (SpeechletResponse) intentMethod.invoke(speechletBean, requestEnvelope);
            } else {
                // try to fill in slots with annotatations
                Map<String, com.amazon.speech.slu.Slot> slots = intent.getSlots();
                Object[] parameterValues = new Object[intentMethod.getParameterCount()];
                Parameter[] parameters = new Parameter[intentMethod.getParameterCount()];
                for ( int paramIdx = 0 ; paramIdx < parameters.length ; paramIdx++ ) {
                    Slot slot = AnnotationUtils.findAnnotation(parameters[paramIdx], Slot.class);
                    if ( slot != null ) {
                        parameterValues[paramIdx] = slots.get(slot.value());
                    }
                }
                return (SpeechletResponse) intentMethod.invoke(speechletBean, parameterValues) ;
            }
        } catch ( IllegalAccessException accessEx ) {
            throw new RuntimeException("Non-public method was annotated as a handler for this request", accessEx);
        } catch ( InvocationTargetException targetEx ) {
            throw new RuntimeException("Invoked method through an unhandled exception", targetEx );
        }
    }

    @Override
    public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> requestEnvelope) {
        Session session = requestEnvelope.getSession();
        Context context = requestEnvelope.getContext();
        SessionEndedRequest request = requestEnvelope.getRequest();

        try {
            Class<?> parameterTypes[] = sessionEndedMethod.getParameterTypes();
            if (parameterTypes.length > 1 && parameterTypes[0].equals(SessionStartedRequest.class) && parameterTypes[1].equals(Session.class)) {
                sessionEndedMethod.invoke(requestEnvelope.getRequest(), requestEnvelope.getSession());
            } else {
                sessionEndedMethod.invoke(speechletBean, requestEnvelope);
            }
        } catch ( IllegalAccessException accessEx ) {
            throw new RuntimeException("Non-public method was annotated as a handler for this request", accessEx);
        } catch ( InvocationTargetException targetEx ) {
            throw new RuntimeException("Invoked method through an unhandled exception", targetEx );
        }
    }

    public Object getSpeechletBean() {
        return speechletBean;
    }

}
