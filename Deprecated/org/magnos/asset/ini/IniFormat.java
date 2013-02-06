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

import java.io.InputStream;

import org.magnos.asset.AssetInfo;
import org.magnos.asset.base.BaseAssetFormat;

/**
 * 
 * @author Philip Diffenderfer
 *
 */
public class IniFormat extends BaseAssetFormat 
{

	/**
	 * 
	 */
	public IniFormat()
	{
		super (new String[] {"ini"}, Ini.class);
	}
	
	@Override
	public AssetInfo getInfo(Class<?> type) 
	{
		return new IniInfo( type );
	}

	@Override
	public Ini loadAsset(InputStream input, AssetInfo assetInfo) throws Exception 
	{
		IniInfo info = (IniInfo)assetInfo;
		
		IniReader reader = new IniReader(input, info.getCharsetName());
		
		return reader.parse();
	}
	

}
