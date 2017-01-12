package org.projectfloodlight.openflow.types;

import java.util.Arrays;
import java.lang.StringBuilder;

import io.netty.buffer.ByteBuf;
import org.projectfloodlight.openflow.exceptions.OFParseError;
import org.projectfloodlight.openflow.protocol.Writeable;

import com.google.common.hash.PrimitiveSink;
import com.google.common.collect.ComparisonChain;

public class McsRxMask implements Comparable<McsRxMask>, Writeable, PrimitiveSinkable {
    static final int length = 10;

    private byte[] mask = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
                                    (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, };

    public static final McsRxMask NONE = new McsRxMask();

    private McsRxMask() {}

    private McsRxMask(byte[] mask) {
        this.mask = mask;
    }

    public int getLength() {
        return length;
    }

    public byte[] getMask() {
        return mask;
    }

    public void write10Bytes(ByteBuf bb) {
        for (byte b : mask) {
            bb.writeByte(b);
        }
    }

    public static McsRxMask read10Bytes(ByteBuf bb) throws OFParseError {
        byte[] mask = new byte[10];
        for (int i = 0; i< 10; i++) {
            mask[i] = (byte) bb.readUnsignedByte();
        }
        return new McsRxMask(mask);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return Arrays.equals(this.mask, ((McsRxMask)o).mask);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.mask);
    }

    @Override
    public String toString() {
        return String.format("McsRxMask {%02x, %02x, %02x, %02x, %02x, %02x, %02x, %02x, %02x, %02x}",
            mask[0], mask[1], mask[2], mask[3], mask[4], mask[5], mask[6], mask[7], mask[8], mask[9]);
    }

    @Override
    public int compareTo(McsRxMask o) {
        ComparisonChain cc = ComparisonChain.start();
        for (int i = 0; i < 10; i++) {
            cc.compare(this.mask[i], o.mask[i]);
        }
        return cc.result();
    }


    @Override
    public void putTo(PrimitiveSink sink) {
        for (byte b : mask) {
            sink.putByte(b);
        }
    }

    @Override
    public void writeTo(ByteBuf bb) {
        for (byte b : mask) {
            bb.writeByte(b);
        }
    }
}
