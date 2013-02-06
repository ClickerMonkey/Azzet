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

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.magnos.asset.ini.Ini;
import org.magnos.asset.ini.IniFormat;
import org.magnos.asset.ini.Section;
import org.magnos.asset.source.ClasspathSource;

public class TestIni 
{

	@Before
	public void onBefore() 
	{
		Assets.addFormat(new IniFormat());
		Assets.addSource("cls", new ClasspathSource());
		Assets.setDefaultSource("cls");
	}
	
	@Test
	public void testIni()
	{
		Ini ini = Assets.load("config.ini");
		
		assertNotNull( ini );
		
		Section global = ini.getGlobal();
		Section owner = ini.get("owner");
		Section database = ini.get("database");
		Section other = ini.get("other");

		assertNotNull( global );
		assertNotNull( owner );
		assertNotNull( database );
		assertNotNull( other );
		
		assertMapEquals( global, new String[][] {
				{"sectionLess", "jenkees"},
				{"name", "John Doe"},
				{"organization", "Acme Widgets Inc."},
				{"server", "192.0.2.62"},
				{"port", "143"},
				{"file", "payroll.dat"},
				{" with spaces ", " yes indeed! "},
				{"trimmed", "meow 1 2 3"},
				{"onsameline", " yes mam "},
				{"with\\escape", "\n\t\r\0;#=[]H"},
				{"pretty=nice", "right?"},
				{"but", "I have one!"},
				{"novaluewithcomment", ""},
		});
		
		assertMapEquals( owner, new String[][] {
				{"name", "John Doe"},
				{"organization", "Acme Widgets Inc."},
				{"server", "localhost"},
		});
		
		assertMapEquals( database, new String[][] {
				{"server", "192.0.2.62"},
				{"port", "143"},
				{"file", "payroll.dat"},
				{" with spaces ", " yes indeed! "},
				{"trimmed", "meow 1 2 3"},
		});
		
		assertMapEquals( other, new String[][] {
				{"onsameline", " yes mam "},
				{"with\\escape", "\n\t\r\0;#=[]H"},
				{"pretty=nice", "right?"},
				{"novalue", ""},
				{"but", "I have one!"},
				{"novaluewithcomment", ""},
		});
	}
	
	private void assertMapEquals( Map<String, String> map, String[][] values)
	{
		assertEquals( map.size(), values.length );
		
		for (String[] pair : values)
		{
			assertTrue( map.containsKey(pair[0]) );
			assertEquals( pair[1], map.get(pair[0]) );
		}
	}
	
}
