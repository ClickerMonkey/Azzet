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

package org.magnos.asset.audio;

import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import org.magnos.asset.AssetInfo;
import org.magnos.asset.base.BaseAssetFormat;


/**
 * A format for loading {@link Clip}s from WAV, AU, and AIFF files.
 * 
 * <h2>Extensions</h2>
 * <ul>
 * <li>WAV - Waveform Audio File Format</li>
 * <li>AU - Sun Audio File Format</li>
 * <li>SND - Digital Sound File</li>
 * <li>AIFF - Audio Interchange File Format</li>
 * </ul>
 * 
 * <h2>Request Types<h2>
 * <ul>
 * <li>{@link javax.sound.sampled.Clip}</li>
 * </ul>
 * 
 * @author Philip Diffenderfer
 * 
 */
public class AudioFormat extends BaseAssetFormat
{

	/**
	 * Instantiates a new AudioFormat.
	 */
	public AudioFormat()
	{
		super( new String[] { "wav", "au", "snd", "aiff" }, Clip.class );
	}

	@Override
	public Clip loadAsset( InputStream input, AssetInfo assetInfo ) throws Exception
	{
		AudioInputStream ais = AudioSystem.getAudioInputStream( input );
		Clip clip = AudioSystem.getClip();
		clip.open( ais );
		return clip;
	}

}
