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

package org.magnos.asset.xml;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.magnos.asset.AssetInfo;
import org.magnos.asset.base.BaseAssetFormat;
import org.w3c.dom.Document;


/**
 * A format for loading {@link Document}s from XML files.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class XmlFormat extends BaseAssetFormat
{

	/**
	 * Instantiates a new XmlFormat.
	 */
	public XmlFormat()
	{
		super( new String[] { "xml" }, Document.class );
	}

	@Override
	public AssetInfo getInfo( Class<?> type )
	{
		return new XmlInfo( type );
	}

	@Override
	public Document loadAsset( InputStream input, AssetInfo assetInfo ) throws Exception
	{
		XmlInfo info = (XmlInfo)assetInfo;

		// create and setup the factory.
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating( info.isValidating() );
		factory.setIgnoringComments( info.isIgnoreComments() );
		factory.setIgnoringElementContentWhitespace( info.isIgnoreWhitespace() );
		factory.setCoalescing( info.isCoalescing() );
		factory.setNamespaceAware( info.isNamespaceAware() );
		factory.setExpandEntityReferences( info.isExpandReferences() );

		// create the builder
		DocumentBuilder builder = factory.newDocumentBuilder();

		// parse the input with the given format.
		return builder.parse( input );
	}

}
