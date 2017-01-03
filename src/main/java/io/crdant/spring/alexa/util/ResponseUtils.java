package io.crdant.tfinder.util;

import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;
import org.springframework.beans.factory.annotation.Value;

public class ResponseUtils {
    public static SpeechletResponse simpleTextResponse(String text) {
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(text);

        SimpleCard card = new SimpleCard();
        card.setTitle("The T Finder");
        card.setContent(text);

        return SpeechletResponse.newTellResponse(speech, card);
    }
}
