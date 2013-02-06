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

package org.magnos.asset.image;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

import org.magnos.asset.AssetInfo;
import org.magnos.asset.base.BaseAssetFormat;


/**
 * A format for loading {@link BufferedImage}[] from GIF files.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class GifFormat extends BaseAssetFormat
{

	/**
	 * Instantiates a new GifFormat.
	 */
	public GifFormat()
	{
		super( new String[] { "gif" }, BufferedImage[].class );
	}

	@Override
	public BufferedImage[] loadAsset( InputStream input, AssetInfo assetInfo ) throws Exception
	{
		// Get the image reader...
		Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix( "gif" );

		// Check that there is one (this should almost never fail).
		if (!iter.hasNext())
		{
			throw new RuntimeException( "gif image reader not found" );
		}

		// Take the GIF reader
		ImageReader image = iter.next();

		// Set the input stream to which to read the GIF from.
		image.setInput( ImageIO.createImageInputStream( input ) );

		// Calculate the number of frames in the GIF image.
		int imgs = image.getNumImages( true );

		// Read each frame from the GIF image.
		BufferedImage[] frames = new BufferedImage[imgs];

		for (int i = 0; i < imgs; i++)
		{
			frames[i] = image.read( i );
		}

		return frames;
	}

}
