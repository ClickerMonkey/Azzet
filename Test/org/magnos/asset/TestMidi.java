/* 
 * NOTICE OF LICENSE
 * 
 * This source file is subject to the Open Software License (OSL 3.0) that is 
 * bundled with this package in the file LICENSE.txt. It is also available 
 * through the world-wide-web at http://opensource.org/licenses/osl-3.0.php
 * If you did not receive a copy of the license and are unable to obtain it 
 * through the world-wide-web, please send an email to magnos.software@gmail.com 
 * so we can send you a copy immediately. If you use any of this software please
 * notify me via our website or email, your feedback is much appreciated. 
 * 
 * @copyright   Copyright (c) 2011 Magnos Software (http://www.magnos.org)
 * @license     http://opensource.org/licenses/osl-3.0.php
 * 				Open Software License (OSL 3.0)
 */

package org.magnos.asset;

import static org.junit.Assert.*;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

import org.junit.Before;
import org.junit.Test;
import org.magnos.asset.audio.MidiFormat;
import org.magnos.asset.source.ClasspathSource;

/**
 * Tests the {@link MidiFormat} class.
 * 
 * @author Philip Diffenderfer
 *
 */
public class TestMidi 
{

	@Before
	public void onBefore()
	{
		Assets.addFormat( new MidiFormat() );
		Assets.setDefaultSource( new ClasspathSource() );
	}
	
	@Test
	public void testMidi()
	{
		try 
		{
			Sequence seq = Assets.load("blaster_master_intro.mid");

			assertNotNull( seq );

			Sequencer player = MidiSystem.getSequencer();

			player.open();
			player.setSequence(seq);
			player.setLoopCount(0);
			player.start();
			
			Thread.sleep(40000);
			
			player.stop();
			player.close();
		}
		catch (MidiUnavailableException e) 
		{
			// ignore, system doesn't have musical output devices!
			System.err.println("Could not play Midi, no audio devices found!");
		}
		catch (InvalidMidiDataException e) 
		{
			fail( e.getMessage() );
		}
		catch (InterruptedException e)
		{
			fail( e.getMessage() );
		}
	}
	
}
