package com.assanguino.adventofcode2021;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;

public class PacketDecoder implements Executable {
    
    protected static final int LENGTH_VERSION = 3;
    protected static final int LENGTH_TYPE_ID = 3;
    protected static final int LENGTH_LITERAL_VALUE = 5;

    protected Part part;
    protected String hexadecimal = "";
    protected String processingRow = "";

    protected Packet initialPacket = new Packet();

    public PacketDecoder(Part part) {
        this.part = part;
    }

    public void processRow(String row) {
        // guard for comments
        if(row.charAt(0) == '#')
            return;

        hexadecimal = row;
    }

    public void execute() {
        processingRow = DecodeUtils.hexToBinary(hexadecimal);
        processPacket(0, initialPacket);
    }

    public String printDescription() {
        return (part == Part.first) ? 
            "Packet Decoder - What do you get if you add up the version numbers in all packets ?" : 
            "Packet Decoder - What do you get if you evaluate the expression represented by your hexadecimal-encoded BITS transmission ?";
    }

    public void printResult() {
        logger.info("");
        if(part == Part.first)
            logger.info("The version total sum is %ld", initialPacket.getAddUpVersion());
        else
            logger.info("The version total value is %ld", initialPacket.getValue());
        logger.info("");

        logger.info("");
    }

    public String getResult() {
        return part == Part.first ?
            String.valueOf(initialPacket.getAddUpVersion()) :
            String.valueOf(initialPacket.getValue()) ;
    }

    protected Long getVersion(String string) {
        return DecodeUtils.binaryToLong(string, 0, LENGTH_VERSION);
    }

    protected Long getTypeID(String string) {
        return DecodeUtils.binaryToLong(string, 3, LENGTH_TYPE_ID);
    }

    protected Integer processPacket(Integer index, Packet packet) {

        String string = processingRow.substring(index);

        long version = getVersion(string);
        long typeID = getTypeID(string);

        String typeDescription = typeID == 4 ? "literal value" : "operator packet";
        logger.printf(Level.INFO, "processing packet - version %d, typeID %d (%s) - index %d - processing from index %d", 
            version, typeID, typeDescription, index, index + LENGTH_VERSION + LENGTH_TYPE_ID);

        packet.version = version;
        packet.typeID = typeID;

        Integer result;
        if(typeID == 4) {
            // literal value
            result = processLiteralValue(index + LENGTH_VERSION + LENGTH_TYPE_ID, packet);
        } else {
            // operator
            result = processOperator(index + LENGTH_VERSION + LENGTH_TYPE_ID, packet);
        }

        return result;
    }

    protected Integer processLiteralValue(Integer index, Packet packet) {
        Integer initIndex = index;
        String sub = "";
        StringBuilder binaryResult = new StringBuilder();

        while(processingRow.charAt(index++) == '1') {
            sub = processingRow.substring(index, index + LENGTH_LITERAL_VALUE - 1);
            logger.printf(Level.DEBUG, "     literal value substring [%s]", sub);
            binaryResult.append(sub);
            index += (LENGTH_LITERAL_VALUE-1);
        }

        // Just get the last group (headed with '0')
        sub = processingRow.substring(index, index + LENGTH_LITERAL_VALUE - 1);
        logger.printf(Level.DEBUG, "     literal value substring [%s]", sub);
        binaryResult.append(sub);
        index += (LENGTH_LITERAL_VALUE-1);

        String result = binaryResult.toString();
        Long total = DecodeUtils.binaryToLong(result, 0, result.length());
        logger.printf(Level.INFO, "     literal value total %d - init index %d - next index %d", total, initIndex, index);

        packet.sum = total;

        return index;
    }

    protected Integer processOperator(Integer index, Packet packet) {

        long totalLength = -1;
        long noSubPackets = -1;

        // checking the length type ID
        if(processingRow.charAt(index++) == '0') {
            totalLength = DecodeUtils.binaryToLong(processingRow, index, 15);
            index += 15;

            logger.printf(Level.INFO, "     process operator labeled I = '0' - length of sub-packets %d - index %d", totalLength, index);

            int initIndex = index;
            while(index < initIndex + totalLength) {
                logger.printf(Level.DEBUG, "     * process operator packet, from index %d - length left %d", index, initIndex + totalLength - index);

                Packet subPacket = new Packet();
                packet.packets.add(subPacket);
    
                index = processPacket(index, subPacket);
            }

        } else {
            noSubPackets = DecodeUtils.binaryToLong(processingRow, index, 11);
            index += 11;

            logger.printf(Level.INFO, "     process operator labeled I = '1' - total amount of sub-packets %d - index %d", noSubPackets, index);

            while(noSubPackets > 0) {
                logger.printf(Level.DEBUG, "     * process operator packet, from index %d - sub-packets to be processed: %d", index, noSubPackets);

                Packet subPacket = new Packet();
                packet.packets.add(subPacket);

                index = processPacket(index, subPacket);
                noSubPackets--;
            }
        }

        return index;
    }

    protected static class DecodeUtils {

        private DecodeUtils() {
            throw new IllegalStateException("Utility class");
        }
        
        public static String hexToBinary(String hex) {

            String binary = hex;
    
            binary = binary.replace("0", "0000");
            binary = binary.replace("1", "0001");
            binary = binary.replace("2", "0010");
            binary = binary.replace("3", "0011");
            binary = binary.replace("4", "0100");
            binary = binary.replace("5", "0101");
            binary = binary.replace("6", "0110");
            binary = binary.replace("7", "0111");
            binary = binary.replace("8", "1000");
            binary = binary.replace("9", "1001");
            binary = binary.replace("A", "1010");
            binary = binary.replace("B", "1011");
            binary = binary.replace("C", "1100");
            binary = binary.replace("D", "1101");
            binary = binary.replace("E", "1110");
            binary = binary.replace("F", "1111");

            return binary;
        }
    
        public static Long binaryToLong(String string, int index, int size) {
            return Long.parseLong(string.substring(index, index + size), 2);
        }
    
    }

    // enum of comparison operations
    public enum PacketComparisonOps {
        GREATER_THAN(5),
        LESS_THAN(6),
        EQUAL_TO(7);

        public final long value;

        private PacketComparisonOps(long value) {
            this.value = value;
        }
    }

    // Class that represent the packets from the hexadecimal string

    class Packet {

        private static final String FATAL_MSG = "The packet hasn't 2 sub-packets to compare! (%d indeed)";

        protected long version;
        protected long typeID;
        protected long sum;
        protected List<Packet> packets;

        public Packet() {
            version = -1;
            typeID = -1;
            sum = 0;
            packets = new ArrayList<>();
        }

        public long getAddUpVersion() {
            if(version == -1)
                return 0;

            long totalSum = version;
            for (Packet packet : packets)
                totalSum += packet.getAddUpVersion();

            return totalSum;
        }

        public long getValue() {

            long result = 0;

            if(typeID == 0) {
                // sum of the sub-packets
                result = subPacketsSum();

            } else if(typeID == 1) {
                // product of the sub-packets 
                result = subPacketsProduct();

            } else if(typeID == 2) {
                // minimum
                result = subPacketsMin();

            } else if(typeID == 3) {
                // maximum
                result = subPacketsMax();

            } else if(typeID == 5 || typeID == 6 || typeID == 7) {

                return compareListHead(typeID);
            }

            // Literal values
            return (typeID == 4) ? sum : result;
        }

        protected long subPacketsSum() {
            long result = 0;

            for (Packet packet : packets)
                result += packet.getValue();

            return result;
        }

        protected long subPacketsProduct() {
            long result = 0;

            for (Packet packet : packets)
                result *= packet.getValue();

            return result;
        }

        protected long subPacketsMin() {
            long result = Long.MAX_VALUE;

            for (Packet packet : packets)
                result = Math.min(result, packet.getValue());

            return result;
        }

        protected long subPacketsMax() {
            long result = Long.MIN_VALUE;

            for (Packet packet : packets)
                result = Math.max(result, packet.getValue());

            return result;
        }

        protected long compareListHead(long iOp) {
            long result = -1;

            if(packets.size() != 2) {
                logger.fatal(FATAL_MSG, packets.size());
                return result;
            }

            long value0 = packets.get(0).getValue();
            long value1 = packets.get(1).getValue();

            if(PacketComparisonOps.GREATER_THAN.value == iOp) {
                result = value0 > value1 ? 1 : 0;
            } else if(PacketComparisonOps.LESS_THAN.value == iOp) {
                result = value0 < value1 ? 1 : 0;
            } else if(PacketComparisonOps.EQUAL_TO.value == iOp) {
                result = value0 == value1 ? 1 : 0;
            }

            return result;
        }

    }

}
