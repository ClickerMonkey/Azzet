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

package org.magnos.asset.ini;

import java.util.HashMap;

/**
 * 
 * @author Philip Diffenderfer
 *
 */
public class Ini extends HashMap<String, Section>
{
	
	private static final long serialVersionUID = 1L;
	
	private Section global;
	
	public Ini()
	{
		global = new Section("%GLOBAL%");
	}
	
	/**
	 * 
	 * @return
	 */
	public Section getGlobal()
	{
		return global;
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public String getProperty(String name)
	{
		return global.get(name);
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public boolean hasProperty(String name)
	{
		return global.containsKey(name);
	}
	
	/**
	 * 
	 * @param name
	 * @param section
	 * @return
	 */
	public String getProperty(String name, String section)
	{
		Section s = get(section);
		
		return (s == null ? null : s.get(name));
	}
	
	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void addProperty(String name, String value)
	{
		global.put(name, value);
	}
	
	/**
	 * 
	 * @param name
	 * @param value
	 * @param section
	 */
	public void addProperty(String name, String value, String section)
	{
		global.put(name, value);
		
		Section s = get(section);
		if (s == null) {
			s = new Section(section);
			put(section, s);
		}
		s.put(name, value);
	}
	
	/**
	 * 
	 * @param s
	 * @return
	 */
	public Section add(Section s)
	{
		return put(s.getName(), s);
	}
	
	/**
	 * 
	 */
	public Section put(String name, Section s)
	{
		if (!name.equals(s.getName())) 
		{
			throw new RuntimeException();
		}
		
		Section p = super.put(name, s);
		
		if (p != null) {
			reset();
		}
		else {
			global.putAll(s);	
		}
		
		return p;
	}
	
	/**
	 * 
	 */
	public Section remove(Object name)
	{
		Section s = super.remove(name);
		reset();
		return s;
	}
	
	/**
	 * 
	 */
	private void reset()
	{
		global.clear();
		for (Section x : values()) { 
			global.putAll(x);
		}
	}
	
}
