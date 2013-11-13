<h1>Azzet</h1>

![Stable](http://i4.photobucket.com/albums/y123/Freaklotr4/stage_stable.png)

Azzet is a simple Java library for loading assets from various sources

```java
BufferedImage img = Assets.load("http://www.google.com/logos/classicplus.png"); // loaded from website
Font fnt = Assets.load("myfont.ttf", new FontInfo(32.0f)); // loaded from classpath
Clip snd = Assets.load("C:\UserData\MyMusic.wav"); // loaded from file-system
BufferedImage gif = Assets.loadFrom("mygif.gif", BufferedImage.class); // you can request the return type
BufferedImage[] animatedGif = Assets.loadFrom("mygif.gif", "db"); // loads from DatabaseSource saved as "db"
Properties props = Assets.loadFrom("app.properties", "tcp"); // loads from TcpSource saved as "tcp"
```

<b>Formats (and equivalent java objects):</b>
- <b>JSON</b> ([org.magnos.asset.json.JsonValue](Formats/org/magnos/asset/json/JsonValue.java))
- <b>XML</b> (org.w3c.dom.Document)
- <b>PROPERTIES/XML/CONFIG</b> (java.util.Properties, [org.magnos.asset.props.Config](Formats/org/magnos/asset/props/Config.java))
- <b>GIF</b> (java.awt.image.BufferedImage[])
- <b>PNG/BMP/WBMP/JPEG/JPG</b> (java.awt.image.BufferedImage)
- <b>MID/MIDI</b> (javax.sound.midi.Sequence)
- <b>WAV/AU/AIFF/SND</b> (javax.sound.sampled.Clip)
- <b>TTF</b> (java.awt.Font)
- <b>CSV</b> ([org.magnos.asset.csv.Table](Formats/org/magnos/asset/csv/Table.java))
- <b>CLASS/CLAZZ</b> (java.lang.Class)
- <b>JAR</b> ([org.magnos.asset.java.Jar](Formats/org/magnos/asset/java/Jar.java))
- <b>ZIP</b> ([org.magnos.asset.zip.Zip](Formats/org/magnos/asset/zip/Zip.java))
- <b>GZ</b> ([org.magnos.asset.AssetInfo](Source/org/magnos/asset/AssetInfo.java))
- <b>DAT</b> (byte[], java.io.InputStream, java.nio.ByteBuffer, java.io.ByteArrayOutputStream)
- <b>TXT</b> (java.lang.String, char[], java.nio.CharBuffer, java.lang.StringBuffer, java.lang.StringBuilder)

<b>Sources:</b>
- Classpath
- File-System
- Database
- JAR
- FTP
- HTTP/HTTPS
- UDP
- TCP
- SSL
- UDP Multicast 

You can also load assets in the background:

```java
FutureAsset<BufferedImage> future = Assets.loadFuture("mypic.png");
BufferedImage mypic = null;

while (running) {
  // do stuff
  if ( future.getStatus() == FutureAssetStatus.Loaded ) {
     future.loaded(); // mark the future as loaded
     mypic = future.get();  // get the loaded asset
  }
  // do other stuff
}
```

This type of deferred/lazy loading is especially useful for applications like games with loading screens. You can bundle all of the FutureAssets up to easily manage several assets loading in the background:

```java
FutureAssetBundle bundle = new FutureAssetBundle();
bundle.add( Assets.loadFuture("image.gif", BufferedImage.class) );
bundle.add( Assets.loadFuture("sound.wav", Clip.class) );

BufferedImage image = null;
Clip sound = null;

// game loop
while (running) {
   // do stuff
   // this occurs during the loading screen....
   if (bundle.isComplete()) {
       bundle.loaded(); // notify all FutureAsset implementations the asset has been accepted.
       image = bundle.getAsset("image.gif");
       sound = bundle.getAsset("sound.wav");
       // move from loading to play screen
   } else {
       display bundle.percentComplete();     
   }
   // do other stuff
}
```

<b>Links</b>:
- [Documentation](http://gh.magnos.org/?r=http://clickermonkey.github.com/azzet/) 
- [Builds](build)
- [Examples] (Test/org/magnos/asset)

The Formats and Sources are registered with the Assets class. The Assets class determines the format and source to use for a given request based on extension, requested asset type, the default source, and any registered sources and completes the request. 

The Asset class also caches assets to avoid re-retrieving and parsing an asset.

The library can be extended further by adding your own AssetFormat and AssetSource implementations.

Checkout the Test source folder for examples on how to load and use assets created by Azzet.
