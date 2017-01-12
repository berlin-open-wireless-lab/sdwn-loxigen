package org.projectfloodlight.openflow.types;

import io.netty.buffer.ByteBuf;

import com.google.common.hash.PrimitiveSink;

public class OFBitMask256 implements OFValueType<OFBitMask256> {

    static final int LENGTH = 32;

    private final long raw1; // (ports 192-255)
    private final long raw2; // (ports 128-191)
    private final long raw3; // (ports 64-127)
    private final long raw4; // (ports 0-63)


    public static final OFBitMask256 ALL = new OFBitMask256(-1, -1, -1, -1);
    public static final OFBitMask256 NONE = new OFBitMask256(0, 0, 0, 0);

    public static final OFBitMask256 NO_MASK = ALL;
    public static final OFBitMask256 FULL_MASK = NONE;

    private OFBitMask256(long raw1, long raw2, long raw3, long raw4) {
        this.raw1 = raw1;
        this.raw2 = raw2;
        this.raw3 = raw3;
        this.raw4 = raw4;
    }

    public static OFBitMask256 of(long raw1, long raw2, long raw3, long raw4) {
        if (raw1 == -1 && raw2 == -1 && raw3 == -1 && raw4 == -1)
            return ALL;
        if (raw1 == 0 && raw2 == 0 && raw3 == 0 && raw4 == 0)
            return NONE;
        return new OFBitMask256(raw1, raw2, raw3, raw4);
    }

    @Override
    public int getLength() {
        return LENGTH;
    }

    @Override
    public OFBitMask256 applyMask(OFBitMask256 mask) {
        return of(this.raw1 & mask.raw1, this.raw2 & mask.raw2, 
                  this.raw3 & mask.raw3, this.raw4 & mask.raw4);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof OFBitMask256))
            return false;
        OFBitMask256 other = (OFBitMask256) obj;
        return (other.raw1 == this.raw1 &&
                other.raw2 == this.raw2 &&
                other.raw3 == this.raw3 &&
                other.raw4 == this.raw4);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (raw1 ^ (raw1 >>> 32));
        result = prime * result + (int) (raw2 ^ (raw2 >>> 32));
        result = prime * result + (int) (raw3 ^ (raw3 >>> 32));
        result = prime * result + (int) (raw4 ^ (raw4 >>> 32));
        return result;
    }

    protected static boolean isBitOn(long raw1, long raw2, long raw3, long raw4, int bit) {
        if (bit < 0 || bit >= 256)
            throw new IndexOutOfBoundsException();
        long word;
        if (bit < 64) {
            word = raw4; // ports 0-63
        } else  if(bit < 128) {
            word = raw3; // ports 64-127
            bit -= 64;
        } else if (bit < 192) {
            word = raw2; // ports 128-191
            bit -= 128;
        } else {
            word = raw1; // ports 192-255
            bit -= 192;
        }
        return (word & ((long)1 << bit)) != 0;
    }

    public void write32Bytes(ByteBuf cb) {
        cb.writeLong(raw1);
        cb.writeLong(raw2);
        cb.writeLong(raw3);
        cb.writeLong(raw4);
    }

    public static OFBitMask256 read32Bytes(ByteBuf cb) {
        long raw1 = cb.readLong();
        long raw2 = cb.readLong();
        long raw3 = cb.readLong();
        long raw4 = cb.readLong();
        return of(raw1, raw2, raw3, raw4);
    }

    public boolean isOn(int bit) {
        return isBitOn(raw1, raw2, raw3, raw4, bit);
    }

    @Override
    public String toString() {
        return (String.format("%64s", Long.toBinaryString(raw4)) +
                String.format("%64s", Long.toBinaryString(raw3)) +
                String.format("%64s", Long.toBinaryString(raw2)) +
                String.format("%64s", Long.toBinaryString(raw1))).replaceAll(" ", "0");
    }

    @Override
    public int compareTo(OFBitMask256 o) {
        long c = this.raw1 - o.raw1;
        if (c != 0)
            return Long.signum(c);
        c = this.raw2 - o.raw2;
        if (c != 0)
            return Long.signum(c);
        c = this.raw3 - o.raw3;
        if (c != 0)
            return Long.signum(c);
        return Long.signum(this.raw4 - o.raw4);
    }

    @Override
    public void putTo(PrimitiveSink sink) {
        sink.putLong(raw1);
        sink.putLong(raw2);
        sink.putLong(raw3);
        sink.putLong(raw4);
    }

}
