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

package org.magnos.asset.applet;


import java.applet.Applet;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

import org.magnos.asset.Assets;
import org.magnos.asset.audio.MidiFormat;
import org.magnos.asset.font.FontFormat;
import org.magnos.asset.font.FontInfo;
import org.magnos.asset.image.GifFormat;
import org.magnos.asset.image.ImageFormat;
import org.magnos.asset.source.ClasspathSource;
import org.magnos.asset.source.WebSource;

public class TestApplet extends Applet
{

	private static final long TIMER_DELAY = 2000; 
	private static final float FONT_SIZE = 24f;
	private static final float ANIM_SPEED = 0.1f;
	
	private static final long serialVersionUID = 1L;

	private Font assetFont;
	private Sequence assetMidi;
	private BufferedImage[] assetGif;
	private BufferedImage assetImage;

	private BufferedImage buffer;
	private Sequencer sequencer;
	private Timer timer;
	private int animationFrame = 0;
	private float animationTime;
	private volatile long currentTime;
	
	public TestApplet()
	{
		Assets.addFormats(new GifFormat(), new MidiFormat(), new FontFormat(), new ImageFormat());

		Assets.addSource("cls", new ClasspathSource());
		Assets.addSource("web", new WebSource("http://www.google.com/"));
	}

	public void init() 
	{ 
		Assets.setDefaultSource("cls");

		assetFont = Assets.load("abaddon.ttf", new FontInfo(FONT_SIZE));
		assetMidi = Assets.load("blaster_master_intro.mid");
		assetGif = Assets.load("dance.gif");

		Assets.setDefaultSource("web");

		assetImage = Assets.load("logos/classicplus.png");
		
		try {
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
			sequencer.setSequence(assetMidi);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				TestApplet.this.repaint();
			}
		}, TIMER_DELAY, (long)(ANIM_SPEED * 250));
		
		setBackground(Color.black);
	}
	
	public void start()
	{
		if (sequencer != null && sequencer.isOpen() && sequencer.getSequence() != null) {
			sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
			sequencer.start();
		}
		currentTime = System.nanoTime();
	}

	public synchronized void paint(Graphics g) 
	{
		long time = System.nanoTime();
		update( (time - currentTime) * 0.000000001f );
		currentTime = time;
		
		BufferedImage buffer = getBuffer();
		
		Graphics2D gr = buffer.createGraphics();
		gr.setColor( getBackground() );
		gr.clearRect( 0, 0, buffer.getWidth(), buffer.getHeight() );
		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		draw( gr );
		
		g.drawImage( buffer, 0, 0, this );
	}
	
	private void update(float elapsed)
	{
		animationTime += elapsed;
		if (animationTime >= ANIM_SPEED) {
			animationFrame = (animationFrame + 1) % assetGif.length;
			animationTime = 0f;
		}
	}
	
	private void draw(Graphics2D gr)
	{
		gr.drawImage( assetImage, 0, 0, this );
		gr.drawImage( assetGif[animationFrame], 10, 70, this );
		gr.setColor( Color.white );
		gr.setFont( assetFont );
		gr.drawString( "Welcome to the Jungle", 10, 140 );
	}

	public void stop()
	{
		if (sequencer != null && sequencer.isRunning()) {
			sequencer.stop();
			sequencer.close();
		}
		timer.cancel();
	}
	
	private BufferedImage getBuffer()
	{
		int width = getWidth();
		int height = getHeight();
		if (buffer == null || buffer.getWidth() != width || buffer.getHeight() != height) {
			buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		}
		return buffer;
	}

}