package de.haupz.basicode.ui;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 * The {@code Sound} class provides basic routines for playing audio.
 */
public class Sound {

    public static final float SAMPLE_FREQUENCY = 44100.0f;

    private static final AudioFormat AUDIO_FORMAT = new AudioFormat(SAMPLE_FREQUENCY, 8, 1, true, false);

    private static final SourceDataLine SDL;

    private static double currentPhase = 0.0;

    static {
        try {
            SDL = AudioSystem.getSourceDataLine(AUDIO_FORMAT);
        } catch (LineUnavailableException lue) {
            throw new RuntimeException(lue);
        }
    }

    /**
     * Play a sound with the given parameters.
     *
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
        byte[] buf = new byte[nSamples];
        double phaseIncrement = 2.0 * Math.PI * frequency / SAMPLE_FREQUENCY;
        for (int i = 0; i < nSamples; ++i) {
            buf[i] = (byte) (Math.sin(currentPhase) * volume);
            currentPhase += phaseIncrement;
            currentPhase %= 2.0 * Math.PI;
        }
        SDL.write(buf, 0, nSamples);
        SDL.drain();
        SDL.stop();
        SDL.close();
    }

}
