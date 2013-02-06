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

package org.magnos.asset.font;

import java.awt.Font;
import java.io.InputStream;

import org.magnos.asset.AssetInfo;
import org.magnos.asset.base.BaseAssetFormat;


/**
 * A format for loading {@link Font}s from TTF files.
 * 
 * <h2>Extensions</h2>
 * <ul>
 * <li>TTF - TrueType Font</li>
 * </ul>
 * 
 * <h2>Request Types<h2>
 * <ul>
 * <li>{@link java.awt.Font}</li>
 * </ul>
 * 
 * @author Philip Diffenderfer
 * 
 */
public class FontFormat extends BaseAssetFormat
{

	/**
	 * Instantiates a new FontFormat.
	 */
	public FontFormat()
	{
		super( new String[] { "ttf" }, Font.class );
	}

	@Override
	public AssetInfo getInfo( Class<?> type )
	{
		return new FontInfo();
	}

	@Override
	public Font loadAsset( InputStream input, AssetInfo assetInfo ) throws Exception
	{
		FontInfo info = (FontInfo)assetInfo;
		Font font = Font.createFont( Font.TRUETYPE_FONT, input );

		// Only derive the font if the info specifies a different format.
		if (font.getSize2D() != info.getSize() || font.getStyle() != info.getStyle())
		{
			font = font.deriveFont( info.getStyle(), info.getSize() );
		}

		return font;
	}

}
