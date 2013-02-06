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

package org.magnos.asset.source.db;

/**
 * 
 * Test Asset loading/saving with a MySQL database.
 * 
 * @author Philip Diffenderfer
 *
 */
public class TestMySQL extends TestDatabase 
{
	
	/*


Server:			http://dev.mysql.com/downloads/installer/
Version:		5.5.14.0
Connector:		http://dev.mysql.com/downloads/connector/j/
				mysql-connector-java-5.1.17-bin.jar
Schema:			assets
User: 			assetuser
Password:		assetpassword
Instructions:	Create the user, grant all access from localhost.


CREATE TABLE assets.asset_table (
	id			INTEGER AUTO_INCREMENT,
	name		VARCHAR(255) NOT NULL,
	asset		BLOB NOT NULL,
	PRIMARY KEY (id)
);

	
	 */

	public TestMySQL()
	{
		super("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/assets", "assetuser", "assetpassword");
	}
	
}
