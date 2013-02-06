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

<b>Links</b>:
- [Docmentation](http://clickermonkey.github.com/azzet/) 
- [Builds](build)
- [Examples] (Test/org/magnos/asset)

The Formats and Sources are registered with the Assets class. The Assets class determines the format and source to use for a given request based on extension, requested asset type, the default source, and any registered sources and completes the request. 

The Asset class also caches assets to avoid re-retrieving and parsing an asset.

The library can be extended further by adding your own AssetFormat and AssetSource implementations.

Checkout the Test source folder for examples on how to load and use assets created by Azzet.
