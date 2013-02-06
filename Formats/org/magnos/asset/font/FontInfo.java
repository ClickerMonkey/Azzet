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

import org.magnos.asset.AssetInfo;
import org.magnos.asset.base.BaseAssetInfo;


/**
 * Options for loading a Font file.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class FontInfo extends BaseAssetInfo
{

	/**
	 * The default size of a font.
	 */
	public static final float DEFAULT_SIZE = 10.0f;

	/**
	 * The default style of a font.
	 */
	public static final Style DEFAULT_STYLE = Style.Plain;

	// properties
	public static final String[] PROPERTIES = {
		"size", "style"
	};
	
	/**
	 * The loaded style of the Font.
	 * 
	 * @author Philip Diffenderfer
	 * 
	 */
	public enum Style
	{
		Plain( Font.PLAIN ),
		Bold( Font.BOLD ),
		Italic( Font.ITALIC );

		private final int constant;

		/**
		 * Instantiates a new Style.
		 * 
		 * @param constant
		 *        The Font style constant.
		 */
		private Style( int constant )
		{
			this.constant = constant;
		}

		/**
		 * The Font style constant.
		 * 
		 * @return The Font style constant.
		 */
		public int getConstant()
		{
			return constant;
		}
	}

	private final float size;
	private final Style style;

	/**
	 * Instantiates a new FontInfo with the default size and style.
	 */
	public FontInfo()
	{
		this( DEFAULT_SIZE, DEFAULT_STYLE );
	}

	/**
	 * Instantiates a new FontInfo with the default style.
	 * 
	 * @param size
	 *        The size of the font in points.
	 */
	public FontInfo( float size )
	{
		this( size, DEFAULT_STYLE );
	}

	/**
	 * Instantiates a new FontInfo with the default size.
	 * 
	 * @param style
	 *        The style of the font.
	 */
	public FontInfo( Style style )
	{
		this( DEFAULT_SIZE, style );
	}

	/**
	 * Instantiates a new FontInfo.
	 * 
	 * @param size
	 *        The size of the font in points.
	 * @param style
	 *        The style of the font.
	 */
	public FontInfo( float size, Style style )
	{
		super( Font.class );

		this.size = size;
		this.style = style;
	}

	@Override
	protected Object getProperty( String name )
	{
		if (name.equals( "size" )) return size;
		if (name.equals( "style" )) return style;

		return null;
	}

	@Override
	public boolean isMatch( AssetInfo info )
	{
		if ( info instanceof FontInfo )
		{
			FontInfo font = (FontInfo)info;
			
			return (
				font.size == size &&
				font.style == style
			);
		}
		
		return false;
	}
	
	@Override
	public String[] getProperties()
	{
		return PROPERTIES;
	}
	
	/**
	 * The style of the font to load.
	 * 
	 * @return The style of the font as a Font constant.
	 */
	public int getStyle()
	{
		return style.getConstant();
	}

	/**
	 * The size of the font to load.
	 * 
	 * @return The size of the font in points.
	 */
	public float getSize()
	{
		return size;
	}

}
