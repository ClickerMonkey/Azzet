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

import javax.xml.parsers.DocumentBuilderFactory;

import org.magnos.asset.AssetInfo;
import org.magnos.asset.base.BaseAssetInfo;

/**
 * Information for loading an XML file.
 * 
 * @author Philip Diffenderfer
 *
 */
public class XmlInfo extends BaseAssetInfo 
{
	/**
	 * The default values for the parsing options.
	 */
	public static final boolean DEFAULT_VALIDATING = false;
	public static final boolean DEFAULT_COMMENTS = true;
	public static final boolean DEFAULT_WHITESPACE = true;
	public static final boolean DEFAULT_COALESCING = true;
	public static final boolean DEFAULT_NAMESPACE = true;
	public static final boolean DEFAULT_EXPANDING = true;

	// properties
	public static final String[] PROPERTIES = {
		"validating", "ignorecomments", "ignorewhitespace", "coalescing", 
		"namespaceaware", "expandreferences"
	};
	
	// parsing options
	private boolean validating;
	private boolean ignoreComments;
	private boolean ignoreWhitespace;
	private boolean coalescing;
	private boolean namespaceAware;
	private boolean expandReferences;
	
	/**
	 * Instantiates a new XmlInfo.
	 */
	public XmlInfo(Class<?> type)
	{
		this(type, DEFAULT_VALIDATING, DEFAULT_COMMENTS, DEFAULT_WHITESPACE, DEFAULT_COALESCING, DEFAULT_NAMESPACE, DEFAULT_EXPANDING);
	}
	
	/**
	 * Instantiates a new XmlInfo.
	 * 
	 * @param validating
	 * 		{@link DocumentBuilderFactory#setValidating(boolean)}
	 * @param ignoreComments
	 * 		{@link DocumentBuilderFactory#setIgnoringComments(boolean)}
	 * @param ignoreWhitespace
	 * 		{@link DocumentBuilderFactory#setIgnoringElementContentWhitespace(boolean)}
	 */
	public XmlInfo(Class<?> type, boolean validating, boolean ignoreComments, boolean ignoreWhitespace)
	{
		this(type, validating, ignoreComments, ignoreWhitespace, DEFAULT_COALESCING, DEFAULT_NAMESPACE, DEFAULT_EXPANDING);
	}
	
	/**
	 * Instantiates a new XmlInfo.
	 * 
	 * @param validating
	 * 		{@link DocumentBuilderFactory#setValidating(boolean)}
	 * @param ignoreComments
	 * 		{@link DocumentBuilderFactory#setIgnoringComments(boolean)}
	 * @param ignoreWhitespace
	 * 		{@link DocumentBuilderFactory#setIgnoringElementContentWhitespace(boolean)}
	 * @param coalescing
	 * 		{@link DocumentBuilderFactory#setCoalescing(boolean)}
	 * @param namespaceAware
	 * 		{@link DocumentBuilderFactory#setNamespaceAware(boolean)}
	 * @param expandReferences
	 * 		{@link DocumentBuilderFactory#setExpandEntityReferences(boolean)}
	 */
	public XmlInfo(Class<?> type, boolean validating, boolean ignoreComments, boolean ignoreWhitespace, boolean coalescing, boolean namespaceAware, boolean expandReferences) 
	{
		super( type );
		
		this.validating = validating;
		this.ignoreComments = ignoreComments;
		this.ignoreWhitespace = ignoreWhitespace;
		this.coalescing = coalescing;
		this.namespaceAware = namespaceAware;
		this.expandReferences = expandReferences;
	}

	@Override
	protected Object getProperty(String name)
	{
		if (name.equals("validating")) return validating;
		if (name.equals("ignorecomments")) return ignoreComments;
		if (name.equals("ignorewhitespace")) return ignoreWhitespace;
		if (name.equals("coalescing")) return coalescing;
		if (name.equals("namespaceaware")) return namespaceAware;
		if (name.equals("expandreferences")) return expandReferences;
		
		return null;
	}

	@Override
	public boolean isMatch( AssetInfo info )
	{
		if ( info instanceof XmlInfo )
		{
			XmlInfo xml = (XmlInfo)info;
			
			return (
				xml.coalescing == coalescing &&
				xml.expandReferences == expandReferences &&
				xml.ignoreComments == ignoreComments &&
				xml.ignoreWhitespace == ignoreWhitespace &&
				xml.namespaceAware == namespaceAware &&
				xml.validating == validating
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
	 * {@link DocumentBuilderFactory#setValidating(boolean)}
	 */
	public boolean isValidating() 
	{
		return validating;
	}

	/**
	 * {@link DocumentBuilderFactory#setValidating(boolean)}
	 */
	public void setValidating(boolean validating) 
	{
		this.validating = validating;
	}

	/**
	 * {@link DocumentBuilderFactory#setIgnoringComments(boolean)}
	 */
	public boolean isIgnoreComments() 
	{
		return ignoreComments;
	}

	/**
	 * {@link DocumentBuilderFactory#setIgnoringComments(boolean)}
	 */
	public void setIgnoreComments(boolean ignoreComments) 
	{
		this.ignoreComments = ignoreComments;
	}

	/**
	 * {@link DocumentBuilderFactory#setIgnoringElementContentWhitespace(boolean)}
	 */
	public boolean isIgnoreWhitespace() 
	{
		return ignoreWhitespace;
	}

	/**
	 * {@link DocumentBuilderFactory#setIgnoringElementContentWhitespace(boolean)}
	 */
	public void setIgnoreWhitespace(boolean ignoreWhitespace) 
	{
		this.ignoreWhitespace = ignoreWhitespace;
	}

	/**
	 * {@link DocumentBuilderFactory#setCoalescing(boolean)}
	 */
	public boolean isCoalescing() 
	{
		return coalescing;
	}

	/**
	 * {@link DocumentBuilderFactory#setCoalescing(boolean)}
	 */
	public void setCoalescing(boolean coalescing) 
	{
		this.coalescing = coalescing;
	}

	/**
	 * {@link DocumentBuilderFactory#setNamespaceAware(boolean)}
	 */
	public boolean isNamespaceAware() 
	{
		return namespaceAware;
	}

	/**
	 * {@link DocumentBuilderFactory#setNamespaceAware(boolean)}
	 */
	public void setNamespaceAware(boolean namespaceAware) 
	{
		this.namespaceAware = namespaceAware;
	}

	/**
	 * {@link DocumentBuilderFactory#setExpandEntityReferences(boolean)}
	 */
	public boolean isExpandReferences() 
	{
		return expandReferences;
	}

	/**
	 * {@link DocumentBuilderFactory#setExpandEntityReferences(boolean)}
	 */
	public void setExpandReferences(boolean expandReferences) 
	{
		this.expandReferences = expandReferences;
	}
	
}
