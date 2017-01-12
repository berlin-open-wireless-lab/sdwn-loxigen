package org.projectfloodlight.openflow.types;

import java.util.Arrays;

import com.google.common.collect.ComparisonChain;
import io.netty.buffer.ByteBuf;
import org.projectfloodlight.openflow.exceptions.OFParseError;

import com.google.common.hash.PrimitiveSink;


public class OduSignalID implements OFValueType<OduSignalID> {

    private short tributaryPortNumber;
    private short tributarySlotNum;
    private byte[] tributarySlotMap;

    // FIXME: this will not work. Constant masks and variable-length types don't go well together.
    public final static OduSignalID NO_MASK = new OduSignalID((short) 0,(short) 0, null);
    public final static OduSignalID FULL_MASK = new OduSignalID((short) 0,(short) 0, null);

    public OduSignalID(short tpn, short length, byte[] tsmap)
    {
        this.tributaryPortNumber = tpn;
        this.tributarySlotNum = length;
        this.tributarySlotMap = tsmap;
    }

    @Override
    public int getLength() {
        return 4 + this.tributarySlotNum;
    }

    public short getTributaryPortNumber() {
        return this.tributaryPortNumber;
    }

    public short getTributarySlotNum() {
        return this.tributarySlotNum;
    }

    public byte[] getTributarySlotMap() {
        return this.tributarySlotMap;
    }

    public void writeToBuffer(ByteBuf bb) {
        bb.writeShort(this.tributaryPortNumber);
        bb.writeShort(this.tributarySlotNum);
        bb.writeBytes(this.tributarySlotMap);
    }

    public static OduSignalID readFromBuffer(ByteBuf bb) throws OFParseError {
        short tpn = (short) bb.readUnsignedShort();
        short tsmapLen = (short) bb.readUnsignedShort();
        byte[] tsmap = new byte[tsmapLen];
        bb.readBytes(tsmap);
        return new OduSignalID(tpn, tsmapLen, tsmap);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OduSignalID that = (OduSignalID) o;

        if (tributaryPortNumber != that.tributaryPortNumber) return false;
        if (tributarySlotNum != that.tributarySlotNum) return false;
        
        return Arrays.equals(this.tributarySlotMap, that.tributarySlotMap);
    }

    @Override
    public int hashCode() {
        int result = (int) this.tributaryPortNumber;
        result = 31 * result + (int) this.tributarySlotNum;
        for (byte b : this.tributarySlotMap) {
            result = 31 * result + (int) b; 
        }
        return result;
    }

    @Override
    public String toString() {
        return "OduSignalID {" +
                "tributaryPortNumber=" + tributaryPortNumber +
                ", tributarySlotNum=" + tributarySlotNum +
                ", tributarySlotMap=" + Arrays.toString(tributarySlotMap) +
                '}';
    }

    // FIXME: what if the mask and this object differ in length?
    @Override
    public OduSignalID applyMask(OduSignalID mask) {
        short tpn = (short) (this.tributaryPortNumber & mask.tributaryPortNumber);
        short tsmapLen = (short) (this.tributarySlotNum & mask.tributarySlotNum);
        byte[] tsmap = new byte[java.lang.Math.max(this.tributarySlotNum, mask.tributarySlotNum)];

        int i;
        for (i = 0; i < java.lang.Math.min(this.tributarySlotNum, mask.tributarySlotNum); i++) {
            tsmap[i] =  (byte) (this.tributarySlotMap[i] & mask.tributarySlotMap[i]);
        }

        if (this.tributarySlotNum > java.lang.Math.min(this.tributarySlotNum, mask.tributarySlotNum)) {
            for (; i < this.tributarySlotNum; i ++) {
                tsmap[i] = this.tributarySlotMap[i];
            }
        } else {
            for (; i < mask.tributarySlotNum; i ++) {
                tsmap[i] = mask.tributarySlotMap[i];
            }
        }

        return new OduSignalID(tpn, tsmapLen, tsmap);
    }


    @Override
    public int compareTo(OduSignalID o) {
        int result = ComparisonChain.start()
                              .compare(tributaryPortNumber, o.tributaryPortNumber)
                              .compare(tributarySlotNum, o.tributarySlotNum)
                              .result();

        if (result == 0) {
            for (int i = 0; i < this.tributarySlotNum; i++) {
                result += this.tributarySlotMap[i] - o.tributarySlotMap[i];
            }
        }

        return result;
    }

    @Override
    public void putTo(PrimitiveSink sink) {
        sink.putShort(tributaryPortNumber);
        sink.putShort(tributarySlotNum);
        for (byte b : tributarySlotMap) {
            sink.putByte(b);
        }
    }



}
