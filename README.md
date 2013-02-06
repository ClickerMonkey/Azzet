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
- JSON => org.magnos.asset.json.JsonValue
- XML => org.w3c.dom.Document
- PROPERTIES/XML/CONFIG => java.util.Properties, org.magnos.asset.props.Config
- GIF => java.awt.image.BufferedImage[]
- PNG/BMP/WBMP/JPEG/JPG => java.awt.image.BufferedImage
- MID/MIDI => javax.sound.midi.Sequence
- WAV/AU/AIFF => javax.sound.sampled.Clip
- TTF => java.awt.Font
- CSV => org.magnos.asset.csv.Table
- CLASS/CLAZZ => java.lang.Class
- JAR => org.magnos.asset.java.Jar
- ZIP => org.magnos.asset.zip.Zip
- GZ => org.magnos.asset.AssetInfo
- DAT => byte[], java.io.InputStream, java.nio.ByteBuffer, java.io.ByteArrayOutputStream
- TXT => java.lang.String, char[], java.nio.CharBuffer

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

The Formats and Sources are registered with the Assets class. The Assets class determines the format and source to use for a given request based on extension, requested asset type, the default source, and any registered sources and completes the request. 

The Asset class also caches assets to avoid re-retrieving and parsing an asset.

The library can be extended further by adding your own AssetFormat and AssetSource implementations.

Checkout the Test source folder for examples on how to load and use assets created by Azzet.
