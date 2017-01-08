package io.crdant.spring.speechlet;

import com.amazon.speech.speechlet.SpeechletV2;
import io.crdant.spring.speechlet.annotation.Speechlet;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationObjectSupport;

import java.util.*;

@Component
public class SpeechletMapping extends WebApplicationObjectSupport implements InitializingBean {

    protected Map<String,List<SpeechletV2>> speechletMap;

    public SpeechletMapping () {
        this.speechletMap = new LinkedHashMap<String,List<SpeechletV2>>();
    }

    public List<SpeechletV2> lookupSpeechlet(String applicationId) {
        logger.debug("looking up speechlet for application " + applicationId);
        logger.debug("entry: " + speechletMap.get(applicationId));
        if( speechletMap.get(applicationId) != null ) return speechletMap.get(applicationId);
        else return speechletMap.get("default");
    }

    /**
     * Detect Speechlets at initialization.
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        initSpeechlets();
    }

    /**
     * Scan the beans in the ApplicationContext for Speechlets, either by their
     * class or because they are annoted as one.
     */
    protected void initSpeechlets() {
        String[] beanNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(getApplicationContext(), Object.class) ;
        Class<?> beanType = null;
        for (String beanName : beanNames) {
            beanType = getApplicationContext().getType(beanName);
            if ( isSpeechlet(beanType) ) {
                Object bean = getApplicationContext().getBean(beanName);
                SpeechletV2 speechlet = ( bean instanceof SpeechletV2 ) ? (SpeechletV2)bean : new SpeechletAdapter(bean);
                String applicationId = "default" ;
                Speechlet annotation = AnnotationUtils.findAnnotation(beanType, Speechlet.class) ;
                if ( annotation != null ) {
                    applicationId = annotation.value()[0];
                }
                logger.info("Mapping application " + applicationId + " to Speechlet bean" + beanName);
                speechletMap.putIfAbsent(applicationId,new ArrayList<SpeechletV2>());
                speechletMap.get(applicationId).add(speechlet);
            }
        }
    }

    protected boolean isSpeechlet(Class<?> beanType) {
        return ( Speechlet.class.isAssignableFrom(beanType)) || ( SpeechletV2.class.isAssignableFrom(beanType)) ||
                ( AnnotationUtils.findAnnotation(beanType, Speechlet.class) != null ) ;
    }

}
