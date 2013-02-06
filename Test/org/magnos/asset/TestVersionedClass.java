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

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.magnos.asset.java.ClassFormat;
import org.magnos.asset.java.Jar;
import org.magnos.asset.java.JarFormat;
import org.magnos.asset.source.ClasspathSource;

/**
 * Tests the {@link ClassFormat} class.
 * 
 * @author Philip Diffenderfer
 *
 */
@Ignore
public class TestVersionedClass 
{

	@BeforeClass
	public static void onBefore()
	{
		Assets.addFormat( new JarFormat() );
		Assets.setDefaultSource( new ClasspathSource() );
	}
	
	@AfterClass
	public static void onAfter()
	{
		Assets.reset();
	}
	
	@Test
	public void testClass() throws Exception
	{
		Jar jar0 = Assets.load("version-unversioned.jar");
		Class<?> cls0 = jar0.getClass("org.magnos.asset.test.VersionedClass");
		cls0.getMethod("execute").invoke(cls0.newInstance());
		
		Jar jar1 = Assets.load("version-1-0.jar");
		Class<?> cls1 = jar1.getClass("org.magnos.asset.test.VersionedClass");
		cls1.getMethod("execute").invoke(cls1.newInstance());
		
		Jar jar2 = Assets.load("version-1-2.jar");
		Class<?> cls2 = jar2.getClass("org.magnos.asset.test.VersionedClass");
		cls2.getMethod("execute").invoke(cls2.newInstance());
	}
	
}
