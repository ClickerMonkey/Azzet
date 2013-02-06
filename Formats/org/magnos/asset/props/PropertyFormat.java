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

package org.magnos.asset.props;

import java.io.InputStream;
import java.util.Properties;

import org.magnos.asset.AssetInfo;
import org.magnos.asset.base.BaseAssetFormat;


/**
 * A format for loading {@link Properties} from PROPERTIES and XML files. If the
 * application also loads other XML files this should be added before the
 * XmlFormat to ensure that any XML files are not loaded by this format.
 * 
 * <h2>Extensions</h2>
 * <ul>
 * <li>PROPERTIES - Java Properties File</li>
 * <li>XML - Java XML Properties File</li>
 * <li>CONFIG - Java Properties File</li>
 * </ul>
 * 
 * <h2>Request Types<h2>
 * <ul>
 * <li>{@link java.util.Properties}</li>
 * <li>{@link org.magnos.asset.props.Config}</li>
 * </ul>
 * 
 * @author Philip Diffenderfer
 * 
 */
public class PropertyFormat extends BaseAssetFormat
{

	/**
	 * Instantiates a new PropertyFormat.
	 */
	public PropertyFormat()
	{
		super( new String[] { "properties", "xml", "config" }, Properties.class, Config.class );
	}

	@Override
	public Properties loadAsset( InputStream input, AssetInfo assetInfo ) throws Exception
	{
		Properties props = null;

		if (assetInfo.isType( Properties.class ))
		{
			props = new Properties();
		}
		if (assetInfo.isType( Config.class ))
		{
			props = new Config();
		}

		if (props != null)
		{
			if (isXml( assetInfo ))
			{
				props.loadFromXML( input );
			}
			else
			{
				props.load( input );
			}
		}

		return props;
	}

	/**
	 * Returns true if the given AssetInfo represents an XML asset.
	 * 
	 * @param info
	 *        The AssetInfo to inspect.
	 * @return True if the AssetInfo ends with XML.
	 */
	private boolean isXml( AssetInfo info )
	{
		return info.getRequest().toLowerCase().endsWith( ".xml" );
	}

}
