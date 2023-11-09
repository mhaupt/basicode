package de.haupz.basicode.ui;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class Sound {

    public static final float SAMPLE_FREQUENCY = 44100.0f;

    private static final AudioFormat AUDIO_FORMAT = new AudioFormat(SAMPLE_FREQUENCY, 8, 1, true, false);

    private static final SourceDataLine SDL;

    private static final byte[] BUF = new byte[1];

    static {
        try {
            SDL = AudioSystem.getSourceDataLine(AUDIO_FORMAT);
        } catch (LineUnavailableException lue) {
            throw new RuntimeException(lue);
        }
    }

    /**
     * Play a sound with the given parameters.
     * @param frequency in Hz.
     * @param duration in milliseconds.
     * @param volume from 0 to 100.
     */
    public static void play(int frequency, int duration, int volume) {
        try {
            SDL.open();
        } catch (LineUnavailableException lue) {
            throw new RuntimeException(lue);
        }
        SDL.start();
        int nSamples = (int) (duration * SAMPLE_FREQUENCY / 1000);
        float p = SAMPLE_FREQUENCY / frequency;
        for (int i = 0; i < nSamples; ++i) {
            double a = i / p * 2 * Math.PI;
            BUF[0] = (byte) (Math.sin(a) * volume);
            SDL.write(BUF, 0, 1);
        }
        SDL.drain();
        SDL.stop();
        SDL.close();
    }

}
