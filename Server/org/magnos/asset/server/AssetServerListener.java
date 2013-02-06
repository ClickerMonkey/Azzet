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

package org.magnos.asset.server;

import java.io.InputStream;

/**
 * A listener to the events of an AssetServer. All events are invoked in the
 * context of the thread, therefore no blocking operations should be performed
 * unless the intentions is to purposefully yield the thread from handling
 * additional requests.
 * 
 * @author Philip Diffenderfer
 *
 */
public interface AssetServerListener
{

	/**
	 * The event that occurs when the server has successfully started. This
	 * is invoked in the context of the thread, therefore any processing in
	 * this method will yield the AssetServer from processing.
	 * 
	 * @param server
	 * 		The AssetServer that started.
	 */
	public void onServerStart(AssetServer server);
	
	/**
	 * The event that occurs when the server has successfully stopped. This
	 * is invoked in the context of the thread, therefore any processing in
	 * this method will yield the AssetServer's thread from stopping.
	 * 
	 * @param server
	 * 		The AssetServer that stopped.
	 */
	public void onServerStop(AssetServer server);

	/**
	 * The event that occurs whenever an Exception is thrown in an AssetServer.  
	 * This is invoked in the context of the thread, therefore any processing 
	 * in this method will yield the AssetServer's thread from processing 
	 * requests.
	 * 
	 * @param server
	 * 		The AssetServer that threw the error.
	 * @param e
	 * 		The exception that was thrown.
	 * @param expected
	 * 		Whether the error was expected. Some errors are expected, for
	 * 		example errors that occur because a socket had to be closed to
	 * 		stop the server.
	 */
	public void onServerError(AssetServer server, Exception e, boolean expected);
	
	/**
	 * The event that occurs when the AssetServer has received a request for an
	 * asset. This is invoked in the context of the thread, therefore any 
	 * processing in this method will yield the AssetServer's thread from 
	 * processing requests.
	 * 
	 * @param server
	 * 		The AssetServer that received the request.
	 * @param request
	 * 		The request that the AssetServer received.
	 */
	public void onAssetRequest(AssetServer server, String request);
	
	/**
	 * The event that occurs when the AssetServer has received a request and 
	 * has located the asset. This is invoked in the context of the thread, 
	 * therefore any processing in this method will yield the AssetServer's 
	 * thread from processing requests.
	 * 
	 * @param server
	 * 		The AssetServer that is sending the response.
	 * @param response
	 * 		The asset as an InputStream.
	 */
	public void onAssetResponse(AssetServer server, String request, InputStream response);
	
}
