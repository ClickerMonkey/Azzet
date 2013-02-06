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

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;

import org.magnos.asset.AssetInfo;
import org.magnos.asset.base.BaseAssetFormat;


/**
 * A format for loading {@link Sequence}s from MIDI (MID) files.
 * 
 * <h2>Extensions</h2>
 * <ul>
 * <li>MID/MIDI - Musical Instrument Digital Interface</li>
 * </ul>
 * 
 * <h2>Request Types<h2>
 * <ul>
 * <li>{@link javax.sound.midi.Sequence}</li>
 * </ul>
 * 
 * @author Philip Diffenderfer
 * 
 */
public class MidiFormat extends BaseAssetFormat
{

	/**
	 * Instantiates a new MidiFormat.
	 */
	public MidiFormat()
	{
		super( new String[] { "mid", "midi" }, Sequence.class );
	}

	@Override
	public Sequence loadAsset( InputStream input, AssetInfo assetInfo ) throws Exception
	{
		return MidiSystem.getSequence( input );
	}

}
