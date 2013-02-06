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
import org.magnos.asset.csv.CsvFormat;
import org.magnos.asset.csv.Table;
import org.magnos.asset.source.ClasspathSource;
import org.magnos.asset.zip.GzipFormat;

/**
 * Tests the {@link GzipFormat} class.
 * 
 * @author Philip Diffenderfer
 *
 */
public class TestGzip 
{

	@Before
	public void onBefore()
	{
		Assets.addFormat( new CsvFormat() );
		Assets.addFormat( new GzipFormat() );
		Assets.setDefaultSource( new ClasspathSource() );
	}
	
	@Test
	public void testArchive()
	{
		AssetInfo tblAsset = Assets.load("data.csv.gz");
		
		assertNotNull( tblAsset );
		
		Table tbl = tblAsset.get();

		assertNotNull( tbl );
		assertEquals( 4, tbl.size() );
	}
	
}
