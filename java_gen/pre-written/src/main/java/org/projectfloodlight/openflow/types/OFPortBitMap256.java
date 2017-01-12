package org.projectfloodlight.openflow.types;

import java.util.ArrayList;

import javax.annotation.concurrent.Immutable;


/** User-facing object representing a bitmap of ports that can be matched on.
 *  This is implemented by the custom BSN OXM type of_oxm_bsn_in_ports_256.
 *
 *  You can call set() on the builder for all the Ports you want to match on
 *  and unset to exclude the port.
 *
 *  <b>Implementation note:</b> to comply with the matching semantics of OXM (which is a logical "AND" not "OR")
 *  the underlying match uses a data format which is very unintuitive. The value is always
 *  0, and the mask has the bits set for the ports that should <b>NOT</b> be included in the
 *  range.
 *
 *  For the curious: We transformed the bitmap (a logical OR) problem into a logical
 *  AND NOT problem.
 *
 *  We logically mean:   Inport is 1 OR 3
 *  We technically say:  Inport IS NOT 2 AND IS NOT 4 AND IS NOT 5 AND IS NOT ....
 *  The second term cannot be represented in OXM, the second can.
 *
 *  That said, all that craziness is hidden from the user of this object.
 *
 *  <h2>Usage</h2>
 *  OFPortBitmap is meant to be used with MatchField <tt>BSN_IN_PORTS_256</tt> in place
 *  of the raw type Masked&lt;OFBitMask256&gt;.
 *
 *  <h3>Example:</h3>:
 *  <pre>
 *  OFPortBitMap portBitMap;
 *  Match.Builder matchBuilder;
 *  // initialize
 *  matchBuilder.setMasked(MatchField.BSN_IN_PORTS_256, portBitmap);
 *  </pre>
 *
 * @author Andreas Wundsam {@literal <}andreas.wundsam@bigswitch.com{@literal >}
 */
@Immutable
public class OFPortBitMap256 extends Masked<OFBitMask256> {

    private OFPortBitMap256(OFBitMask256 mask) {
        super(OFBitMask256.NONE, mask);
    }

    /** 
     * @param port the port to check
     * @return whether or not the given port is logically included in the
     * match, i.e., whether a packet from in-port <em>port</em> be matched by
     * this OXM.
     */
    public boolean isOn(OFPort port) {
        // see the implementation note above about the logical inversion of the mask
        return !(this.mask.isOn(port.getPortNumber()));
    }

    public static OFPortBitMap256 ofPorts(OFPort... ports) {
        Builder builder = new Builder();
        for (OFPort port: ports) {
            builder.set(port);
        }
        return builder.build();
    }

    /** 
     * @param mask the mask used to create the bitmap
     * @return an OFPortBitmap based on the 'mask' part of an OFBitMask256, as, e.g., returned
     * by the switch.
     */
    public static OFPortBitMap256 of(OFBitMask256 mask) {
        return new OFPortBitMap256(mask);
    }

    /** 
     * @return iterating over all ports that are logically included in the
     * match, i.e., whether a packet from in-port <em>port</em> be matched by
     * this OXM.
     */
    public Iterable<OFPort> getOnPorts() {
        ArrayList<OFPort> ports = new ArrayList<>();
        for(int i=0; i < 255; i++) {
            if(!(this.mask.isOn(i))) {
                ports.add(OFPort.of(i));
            }
        }
        return ports;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof OFPortBitMap256))
            return false;
        OFPortBitMap256 other = (OFPortBitMap256)obj;
        return (other.value.equals(this.value) && other.mask.equals(this.mask));
    }

    @Override
    public int hashCode() {
        return 619 * mask.hashCode() + 257 * value.hashCode();
    }

    public static class Builder {
        private long raw1 = -1, raw2 = -1, raw3 = -1, raw4 = -1;

        public Builder() {

        }

        /**
         * @param port the port to check
         * @return whether or not the given port is logically included in the
         * match, i.e., whether a packet from in-port <em>port</em> be matched by
         * this OXM.
         */
        public boolean isOn(OFPort port) {
            // see the implementation note above about the logical inversion of the mask
            return !(OFBitMask256.isBitOn(raw1, raw2, raw3, raw4, port.getPortNumber()));
        }

        /** 
         * remove this port from the match, i.e., packets from this in-port
         * will NOT be matched.
         * @param port the port the remove from the match
         * @return this mutator
         */
        public Builder unset(OFPort port) {
            // see the implementation note above about the logical inversion of the mask
            int bit = port.getPortNumber();
            if (bit < 0 || bit > 255)
                throw new IndexOutOfBoundsException("Port number is out of bounds");
            else if (bit == 255)
                // the highest order bit in the bitmask is reserved. The switch will
                // set that bit for all ports >= 255. The reason is that we don't want
                // the OFPortMap to match all ports out of its range (i.e., a packet
                // coming in on port 581 would match *any* OFPortMap).
                throw new IndexOutOfBoundsException("The highest order bit in the bitmask is reserved.");
            else if (bit < 64) {
                raw4 |= ((long)1 << bit);
            } else if (bit < 128) {
                raw3 |= ((long)1 << bit - 64);
            } else if (bit < 192) {
                raw2 |= ((long)1 << bit - 128);
            } else {
                raw1 |= ((long)1 << bit - 192);
            }
            return this;
        }

        /**
         * add this port from the match, i.e., packets from this in-port
         * will be matched.
         * @param port the port to add to the match
         * @return this mutator
         */
        public Builder set(OFPort port) {
            // see the implementation note above about the logical inversion of the mask
            int bit = port.getPortNumber();
            if (bit < 0 || bit > 255)
                throw new IndexOutOfBoundsException("Port number is out of bounds");
            else if (bit == 255)
                // the highest order bit in the bitmask is reserved. The switch will
                // set that bit for all ports >= 255. The reason is that we don't want
                // the OFPortMap to match all ports out of its range (i.e., a packet
                // coming in on port 581 would match *any* OFPortMap).
                throw new IndexOutOfBoundsException("The highest order bit in the bitmask is reserved.");
            else if (bit < 64) {
                raw4 &= ~((long)1 << bit);
            } else if (bit < 128) {
                raw3 &= ~((long)1 << bit - 64);
            } else if (bit < 192) {
                raw2 &= ~((long)1 << bit - 128);
            } else {
                raw1 &= ~((long)1 << bit - 192);
            }
            return this;
        }

        public OFPortBitMap256 build() {
            return new OFPortBitMap256(OFBitMask256.of(raw1, raw2, raw3, raw4));
        }
    }

}
