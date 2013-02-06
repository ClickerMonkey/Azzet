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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * An annotation to control more than one version of the same class. The class
 * with the higher version number is loaded over the lower versioned classes, if
 * a class is loaded with a lower version then the current version it is
 * ignored.
 * 
 * @author Philip Diffenderfer
 * 
 */
@Target (ElementType.TYPE )
@Retention (RetentionPolicy.RUNTIME )
@Deprecated
/* WIP */
public @interface Versioned
{

	/**
	 * The class version.
	 * 
	 * @return The version of the class as a double.
	 */
	public double value();

}
