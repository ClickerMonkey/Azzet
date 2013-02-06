
package org.magnos.asset.server.ssl;

import java.io.IOException;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;

import org.magnos.asset.server.tcp.TcpServer;


/**
 * An AssetServer for transmitting assets over SSL.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class SslServer extends TcpServer
{

	/**
	 * Instantiates a new SslServer.
	 * 
	 * @param serverPort
	 *        The port to bind to.
	 * @param serverBacklog
	 *        The maximum number of waiting connections to queue.
	 */
	public SslServer( int serverPort, int serverBacklog )
	{
		super( serverPort, serverBacklog );
	}

	@Override
	protected void onBind() throws IOException
	{
		// Instead of a normal ServerSocket get an SSLServerSocket.
		ServerSocketFactory factory = SSLServerSocketFactory.getDefault();
		serverSocket = factory.createServerSocket( serverPort, serverBacklog );
	}

}
