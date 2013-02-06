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

package org.magnos.asset.java;

import org.magnos.asset.zip.Zip;


/**
 * An archive of assets loaded from a JAR file.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class Jar extends Zip implements ClassLoadable
{

	private static final long serialVersionUID = 1L;

	// The ClassLoader for the classes in this Jar.
	private DynamicLoader loader;

	/**
	 * Given the class name this will determine the path to the class in the Jar
	 * file and return it.
	 * 
	 * @param className
	 *        The name of the class.
	 * @return The class with the given name.
	 */
	public Class<?> getClass( String className )
	{
		return getAsset( className.replace( '.', '/' ) + ".class" );
	}

	/**
	 * Given the class name this will determine the path to the class in the Jar
	 * file and instantiate an instance of that class.
	 * 
	 * @param <T>
	 *        The instance type.
	 * @param className
	 *        The name of the class.
	 * @return An instance of the given type.
	 */
	public <T> T getInstance( String className )
	{
		try
		{
			return (T)getClass( className ).newInstance();
		}
		catch (Exception e)
		{
			return null;
		}
	}

	@Override
	public void setClassLoader( DynamicLoader loader )
	{
		this.loader = loader;
	}

	@Override
	public DynamicLoader getClassLoader()
	{
		return loader;
	}

}
