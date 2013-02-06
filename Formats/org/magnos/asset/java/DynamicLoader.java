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

/**
 * An internal ClassLoader which can load a Class from a byte array.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class DynamicLoader extends ClassLoader
{

	/**
	 * Instantiates a new DynamicLoader.
	 */
	public DynamicLoader()
	{
		super( DynamicLoader.class.getClassLoader() );
	}

	/**
	 * Returns the class with the given name, or null if none exists.
	 * 
	 * @param className
	 *        The fully qualified name of the class.
	 * @return The class with the given name, or null if it doesn't exist.
	 */
	public Class<?> getClass( String className )
	{
		try
		{
			return Class.forName( className );
		}
		catch (Exception e)
		{
			return null;
		}
	}

	/**
	 * Loads a Class from the given byte array.
	 * 
	 * @param clazz
	 *        The byte array containing the class' byte code.
	 * @return The Class loaded.
	 * @throws Exception
	 *         An error occurred parsing the InputStream for a Class.
	 */
	public Class<?> loadClass( byte[] clazz ) throws Exception
	{
		return defineClass( null, clazz, 0, clazz.length );
	}

	/**
	 * Creates the class by linking it to this ClassLoader.
	 * 
	 * @param cls
	 *        The class to link.
	 */
	public void linkClass( Class<?> cls )
	{
		resolveClass( cls );
	}

}
