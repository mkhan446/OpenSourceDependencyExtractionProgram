

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

class FileSig {

 /** Unknown file type */
    public static final int UNKNOWN = -1;

    /** File type Java class */
    public static final int CLASSFILE = 0xcafebabe;

    /** File type ZIP archive */
    public static final int ZIPFILE = 0x504b0304;

    /** File type GZIP compressed Data */
    public static final int GZFILE = 0x1f8b0000;

    /** File type Pack200 archive */
    public static final int PACK200FILE = 0xcafed00d;

    private static final int BUFFER_SIZE = 8;

    private final InputStream in;

    private final int type;

    /**
     * Creates a new detector based on the given input. To process the complete
     * original input afterwards use the stream returned by
     * {@link #getInputStream()}.
     * 
     * @param in
     *            input to read the header from
     * @throws IOException
     *             if the stream can't be read
     */
    public FileSig(final InputStream in) throws IOException {
       
        if (in.markSupported()) {
            this.in = in;
        } else {
            this.in = new BufferedInputStream(in, BUFFER_SIZE);
        }
        this.in.mark(BUFFER_SIZE);
        this.type = determineType(this.in);
        this.in.reset();
    }

    private static int determineType(final InputStream in) throws IOException {
        final int header = readInt(in);
        switch (header) {
        case ZIPFILE:
            return ZIPFILE;
        case PACK200FILE:
            return PACK200FILE;
        case CLASSFILE:
          	return CLASSFILE;
            
        }
        if ((header & 0xffff0000) == GZFILE) {
            return GZFILE;
        }
        return UNKNOWN;
    }

    private static int readInt(final InputStream in) throws IOException {
        return in.read() << 24 | in.read() << 16 | in.read() << 8 | in.read();
    }

    /**
     * Returns an input stream instance to read the complete content (including
     * the header) of the underlying stream.
     * 
     * @return input stream containing the complete content
     */
    public InputStream getInputStream() {
        return in;
    }

    /**
     * Returns the detected file type.
     * 
     * @return file type
     */
    public int getType() {
        
        return type;
    }


}