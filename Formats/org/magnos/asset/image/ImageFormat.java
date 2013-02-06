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

import javax.imageio.ImageIO;

import org.magnos.asset.AssetInfo;
import org.magnos.asset.base.BaseAssetFormat;


/**
 * A format for loading {@link BufferedImage}s from PNG, BMP, WBMP, JPEG, and
 * JPG files.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class ImageFormat extends BaseAssetFormat
{

	/**
	 * Instantiates a new ImageFormat.
	 */
	public ImageFormat()
	{
		super( new String[] { "png", "bmp", "wbmp", "jpeg", "jpg" }, BufferedImage.class );
	}

	@Override
	public BufferedImage loadAsset( InputStream input, AssetInfo assetInfo ) throws Exception
	{
		return ImageIO.read( input );
	}

}
