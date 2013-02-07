package org.magnos.asset;

import java.util.HashMap;

/**
 * A bundle of FutureAssets to be loaded. This helps keep track of all assets 
 * loading in the background.
 * 
 * <pre>
 * FutureAssetBundle bundle = new FutureAssetBundle();
 * bundle.add( Assets.loadFuture("image.gif", BufferedImage.class) );
 * bundle.add( Assets.loadFuture("sound.wav", Clip.class) );
 * 
 * BufferedImage image = null;
 * Clip sound = null;
 * 
 * while (running) {
 * 	   // do stuff
 *     
 *     if (bundle.isComplete()) {
 *         bundle.loaded(); // notify all FutureAsset implementations the asset has been accepted.
 *         image = bundle.getAsset("image.gif");
 *         sound = bundle.getAsset("sound.wav");
 *         // move from loading to play screen
 *     } else {
 *         display bundle.percentComplete();     
 *     }
 *     
 *     // do other stuff
 * }
 * </pre>
 * 
 * @author Philip Diffenderfer
 * @see FutureAsset
 */
public class FutureAssetBundle extends HashMap<String, FutureAsset<?>>
{
	
	private static final long serialVersionUID = 1L;

	/**
	 * Returns the count of FutureAssets in the given status.
	 * 
	 * @param status
	 * 		The status to check for.
	 * @return The number of FutureAssets in the given status.
	 */
	public int countOf( FutureAssetStatus status )
	{
		int total = 0;
		
		for ( FutureAsset<?> futureAsset : values() )
		{
			if ( futureAsset.getStatus() == status )
			{
				total++;
			}
		}
		
		return total;
	}
	
	/**
	 * Returns the percent complete (between 0.0 and 1.0).
	 * 
	 * @return The percent complete.
	 */
	public float percentComplete()
	{
		int complete = 0;
		
		for ( FutureAsset<?> futureAsset : values() )
		{
			switch ( futureAsset.getStatus() ) {
			case Loaded:
			case Failed:
			case Canceled:
				complete++;
				break;
			}
		}
		
		return (float)complete / size();
	}
	
	/**
	 * Returns whether this bundle of FutureAssets has finished loading. This 
	 * method will return true if no assets are in the pending or loading 
	 * status. Loaded, Failed, and Canceled assets are all considered completed.
	 * 
	 * @return True if all assets have been processed.
	 */
	public boolean hasCompleted()
	{
		return countOf( FutureAssetStatus.Pending ) == 0 && countOf( FutureAssetStatus.Loading ) == 0;
	}
	
	/**
	 * Calls loaded on all FutureAssets in this bundle. This should only be 
	 * done once and when the user has checked for {@link #hasCompleted()} and 
	 * it returned true.
	 * 
	 * @see FutureAsset#loaded()
	 */
	public void loaded()
	{
		for ( FutureAsset<?> futureAsset : values() )
		{
			futureAsset.loaded();
		}
	}
	
	/**
	 * Calls {@link FutureAsset#load(boolean)} on all FutureAssets in this 
	 * bundle.
	 * 
	 * @param loadIfCanceled
	 *        Whether the asset should be loaded if the FutureAsset has been 
	 *        canceled already. If true the asset will always be loaded, if 
	 *        false the asset will not be loaded if the status is canceled.
	 */
	public void load( final boolean loadIfCanceled )
	{
		for ( FutureAsset<?> futureAsset : values() )
		{
			futureAsset.load( loadIfCanceled );
		}
	}
	
	/**
	 * Returns an asset with the given name. If none exists null is returned.
	 * 
	 * @param name
	 * 		The name of the asset.
	 * @return The asset with the given name, or null if none exists.
	 * @throws ClassCastException
	 *         The FutureAsset doesn't actually produce an asset of the expected type.
	 */
	public <A> A getAsset( String name )
	{
		FutureAsset<A> futureAsset = (FutureAsset<A>)get( name );
		
		return (futureAsset == null ? null : futureAsset.get());
	}
	
	/**
	 * Adds the futureAsset to this bundle using the request as the key.
	 * 
	 * @param futureAsset
	 *        The FutureAsset to add to the bundle.
	 */
	public void add( FutureAsset<?> futureAsset )
	{
		put( futureAsset.getInfo().getRequest(), futureAsset );
	}
	
	/**
	 * Tries to cancel all FutureAssets and returns the number successfully 
	 * canceled.
	 * 
	 * @return The number of FutureAssets successfully canceled.
	 */
	public int cancel()
	{
		int canceled = 0;
		
		for ( FutureAsset<?> futureAsset : values() )
		{
			if ( futureAsset.cancel() )
			{
				canceled++;
			}
		}
		
		return canceled;
	}
	
}
