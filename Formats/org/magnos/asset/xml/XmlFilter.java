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

import java.util.Iterator;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A filtered list of XML child elements of a given root element.
 * 
 * @author Philip Diffenderfer
 *
 * @param <T>
 * 	The type of node being filtered.
 */
public class XmlFilter<T extends Node> implements Iterable<T>, Iterator<T>
{

	private final Node root;
	private final NodeList nodes;
	private final int type;
	private int index;

	public XmlFilter( Node root, int type )
	{
		this( root, root.getChildNodes(), type );
	}

	private XmlFilter( Node root, NodeList nodes, int type )
	{
		this.root = root;
		this.nodes = nodes;
		this.type = type;
	}

	@Override
	public Iterator<T> iterator()
	{
		return new XmlFilter<T>( root, type );
	}

	@Override
	public boolean hasNext()
	{
		return (getNextNode( index ) > index);
	}

	@Override
	public T next()
	{
		return (T)nodes.item( index = getNextNode( index ) );
	}

	private int getNextNode( int i )
	{
		while (++i < nodes.getLength())
		{
			if (nodes.item( i ).getNodeType() == type)
			{
				return i;
			}
		}
		return -1;
	}

	@Override
	public void remove()
	{
		throw new UnsupportedOperationException();
	}

}
