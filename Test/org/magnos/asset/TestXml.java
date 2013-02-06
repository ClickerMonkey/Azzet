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

package org.magnos.asset;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.magnos.asset.source.ClasspathSource;
import org.magnos.asset.xml.XmlFormat;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Tests the {@link XmlFormat} class.
 * 
 * @author Philip Diffenderfer
 *
 */
public class TestXml 
{

	@Before
	public void onBefore()
	{
		Assets.addFormat( new XmlFormat() );
		Assets.setDefaultSource( new ClasspathSource() );
	}
	
	@Test
	public void testClass()
	{
		Document doc = Assets.load("plugin.xml");

		assertNotNull( doc );
		
		Node n0 = doc.getFirstChild();
		
		assertNotNull( n0 );
		assertEquals( "plugin", n0.getNodeName() );
		assertEquals( "test", n0.getAttributes().getNamedItem("name").getNodeValue() );
		
		Node n1 = n0.getChildNodes().item(1);

		assertNotNull( n1 );
		assertEquals( "version", n1.getNodeName() );
		assertEquals( "2.3.0", n1.getFirstChild().getNodeValue() );
	}
	
}
