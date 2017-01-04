package io.crdant.spring.alexa.web;

import com.amazon.speech.speechlet.Speechlet;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by crdant on 1/3/17.
 */
public class SpeechletServletRequest  {
    private Speechlet speechlet ;

    public Speechlet getSpeechlet() {
        return speechlet;
    }

    public void setSpeechlet(Speechlet speechlet) {
        this.speechlet = speechlet;
    }

}
