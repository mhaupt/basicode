package de.haupz.basicode.ui;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;

/**
 * The {@code Sound} class provides basic routines for playing audio.
 */
public class Sound {

    private static final Synthesizer SYNTH;

    private static final MidiChannel CHANNEL;

    private static final int SOUND_SQUARE = 81; // MIDI for square wave synth

    static {
        try {
            SYNTH = MidiSystem.getSynthesizer();
            SYNTH.open();
            CHANNEL = SYNTH.getChannels()[0];
            CHANNEL.programChange(SOUND_SQUARE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Play a sound with the given parameters.
     *
     * @param note as a MIDI note (0..127).
     * @param duration in milliseconds.
     * @param volume from 0 to 100.
     */
    public static void play(int note, int duration, int volume) {
        try {
            CHANNEL.noteOn(note, volume);
            Thread.sleep(duration);
            CHANNEL.noteOff(note);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
