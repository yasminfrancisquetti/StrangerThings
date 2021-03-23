package com.example.strangerthings.model;

import android.util.Log;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class CharacterApiTest
{
    @Test
    public void searchCharacter()
    {
        CharacterApi api = new CharacterApi();

        Thread originalThread = Thread.currentThread();

        final AtomicBoolean activated = new AtomicBoolean(false);

        api.searchCharacter("eleven", character -> {

            Thread callbackThread = Thread.currentThread();

            assertNotSame(originalThread, callbackThread);

            assertNotNull(character);
            assertNotNull(character.getActor());
            assertNotNull(character.getName());

            Log.d("whot", character.toString());

            activated.set(true);

        });

        sleep(5000);

        assertTrue(activated.get());
    }

    @Test
    public void downloadCharacterPicture()
    {
        final CharacterApi subject = new CharacterApi();

        final Thread originalThread = Thread.currentThread();

        final AtomicBoolean activated = new AtomicBoolean(false);

        subject.searchCharacter("eleven", character -> {

            assertNotNull(character);

            final Thread currentThread = Thread.currentThread();

            assertNotSame(originalThread, currentThread);

            subject.downloadCharacterPicture(character, bitmap -> {

                final Thread bitmapThread = Thread.currentThread();

                assertNotSame(originalThread, bitmapThread);

                assertNotNull(bitmap);

                activated.set(true);
            });

        });

        sleep(3000);

        assertTrue(activated.get());


    }

    private void sleep(long milliseconds)
    {
        try {
            Thread.sleep(milliseconds);
        }
        catch (InterruptedException ex) {
            fail("Sleep was interrupted");
        }
    }
}