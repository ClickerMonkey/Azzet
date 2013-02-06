<h1>azzet</h1>
<h6>(by Philip Diffenderfer)</h6>

Azzet is a simple Java library for loading assets from various sources

```java
BufferedImage img = Assets.load("http://www.google.com/logos/classicplus.png"); // loaded from website
Font fnt = Assets.load("myfont.ttf", new FontInfo(32.0f)); // loaded from classpath
Clip snd = Assets.load("C:\UserData\MyMusic.wav"); // loaded from file-system
BufferedImage[] gif = Assets.loadFrom("mygif", "db"); // loads from DatabaseSource saved as "db"
Properties props = Assets.loadFrom("app.properties", "tcp"); // loads from TcpSource saved as "tcp"
```

<b>Formats (and equivalent java objects):</b>
- <b>JSON</b> (org.magnos.asset.json.JsonValue)
- <b>XML</b> (org.w3c.dom.Document)
- <b>PROPERTIES/XML/CONFIG</b> (java.util.Properties, org.magnos.asset.props.Config)
- <b>GIF</b> (java.awt.image.BufferedImage[])
- <b>PNG/BMP/WBMP/JPEG/JPG</b> (java.awt.image.BufferedImage)
- <b>MID/MIDI</b> (javax.sound.midi.Sequence)
- <b>WAV/AU/AIFF</b> (javax.sound.sampled.Clip)
- <b>TTF</b> (java.awt.Font)
- <b>CSV</b> (org.magnos.asset.csv.Table)
- <b>CLASS/CLAZZ</b> (java.lang.Class)
- <b>JAR</b> (org.magnos.asset.java.Jar)
- <b>ZIP</b> (org.magnos.asset.zip.Zip)
- <b>GZ</b> (org.magnos.asset.AssetInfo)
- <b>DAT</b> (byte[], java.io.InputStream, java.nio.ByteBuffer, java.io.ByteArrayOutputStream)
- <b>TXT</b> (java.lang.String, char[], java.nio.CharBuffer)

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

[Documentation](http://clickermonkey.github.com/azzet/)

The Formats and Sources are registered with the Assets class. The Assets class determines the format and source to use for a given request based on extension, requested asset type, the default source, and any registered sources and completes the request. 

The Asset class also caches assets to avoid re-retrieving and parsing an asset.

The library can be extended further by adding your own AssetFormat and AssetSource implementations.

Checkout the Test source folder for examples on how to load and use assets created by Azzet.
