package com.assanguino.adventofcode2021;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;

public class PacketDecoder implements Executable {
    
    protected final int length_version = 3;
    protected final int length_typeID = 3;
    protected final int length_literal_value = 5;

    protected Part part;
    protected String hexadecimal = "";
    protected String processing_row = "";

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
        processing_row = DecodeUtils.hexToBinary(hexadecimal);
        processPacket(0, initialPacket);
    }

    public String printDescription() {
        return (part == Part.first) ? 
            "Packet Decoder - What do you get if you add up the version numbers in all packets ?" : 
            "Packet Decoder - What do you get if you evaluate the expression represented by your hexadecimal-encoded BITS transmission ?";
    }

    public void printResult() {
        System.out.println();
        if(part == Part.first)
            System.out.println("The version total sum is " + initialPacket.getAddUpVersion());
        else
            System.out.println("The version total value is " + initialPacket.getValue());
        System.out.println();
    }

    public String getResult() {
        return part == Part.first ?
            String.valueOf(initialPacket.getAddUpVersion()) :
            String.valueOf(initialPacket.getValue()) ;
    }

    protected Long getVersion(String string) {
        return DecodeUtils.binaryToLong(string, 0, length_version);
    }

    protected Long getTypeID(String string) {
        return DecodeUtils.binaryToLong(string, 3, length_typeID);
    }

    protected Integer processPacket(Integer index, Packet packet) {

        String string = processing_row.substring(index);

        long version = getVersion(string);
        long typeID = getTypeID(string);

        String typeDescription = typeID == 4 ? "literal value" : "operator packet";
        logger.printf(Level.INFO, "processing packet - version %d, typeID %d (%s) - index %d - processing from index %d", 
            version, typeID, typeDescription, index, index + length_version + length_typeID);

        packet.version = version;
        packet.typeID = typeID;

        Integer result;
        if(typeID == 4) {
            // literal value
            result = processLiteralValue(index + length_version + length_typeID, packet);
        } else {
            // operator
            result = processOperator(index + length_version + length_typeID, packet);
        }

        return result;
    }

    protected Integer processLiteralValue(Integer index, Packet packet) {
        Integer init_index = index;
        String sub = "", binaryResult = "";

        while(processing_row.charAt(index++) == '1') {
            sub = processing_row.substring(index, index + length_literal_value - 1);
            logger.printf(Level.DEBUG, "     literal value substring [%s]", sub);
            binaryResult += sub;
            index += (length_literal_value-1);
        };

        // Just get the last group (headed with '0')
        sub = processing_row.substring(index, index + length_literal_value - 1);
        logger.printf(Level.DEBUG, "     literal value substring [%s]", sub);
        binaryResult += sub;
        index += (length_literal_value-1);

        Long total = DecodeUtils.binaryToLong(binaryResult, 0, binaryResult.length());
        logger.printf(Level.INFO, "     literal value total %d - init index %d - next index %d", total, init_index, index);

        packet.sum = total;

        return index;
    }

    protected Integer processOperator(Integer index, Packet packet) {

        long totalLength = -1;
        long noSubPackets = -1;

        // checking the length type ID
        if(processing_row.charAt(index++) == '0') {
            totalLength = DecodeUtils.binaryToLong(processing_row, index, 15);
            index += 15;

            logger.printf(Level.INFO, "     process operator labeled I = '0' - length of sub-packets %d - index %d", totalLength, index);

            int init_index = index;
            while(index < init_index + totalLength) {
                logger.printf(Level.DEBUG, "     * process operator packet, from index %d - length left %d", index, init_index + totalLength - index);

                Packet subPacket = new Packet();
                packet.packets.add(subPacket);
    
                index = processPacket(index, subPacket);
            }

        } else {
            noSubPackets = DecodeUtils.binaryToLong(processing_row, index, 11);
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

        public static String hexToBinary(String hex) {

            String binary = hex;
    
            binary = binary.replaceAll("0", "0000");
            binary = binary.replaceAll("1", "0001");
            binary = binary.replaceAll("2", "0010");
            binary = binary.replaceAll("3", "0011");
            binary = binary.replaceAll("4", "0100");
            binary = binary.replaceAll("5", "0101");
            binary = binary.replaceAll("6", "0110");
            binary = binary.replaceAll("7", "0111");
            binary = binary.replaceAll("8", "1000");
            binary = binary.replaceAll("9", "1001");
            binary = binary.replaceAll("A", "1010");
            binary = binary.replaceAll("B", "1011");
            binary = binary.replaceAll("C", "1100");
            binary = binary.replaceAll("D", "1101");
            binary = binary.replaceAll("E", "1110");
            binary = binary.replaceAll("F", "1111");

            return binary;
        }
    
        public static Long binaryToLong(String string, int index, int size) {
            return Long.parseLong(string.substring(index, index + size), 2);
        }
    
    }

    // Class that represent the packets from the hexadecimal string

    class Packet {
        public long version;
        public long typeID;
        public long sum;
        public List<Packet> packets;

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
                for (Packet packet : packets)
                    result += packet.getValue();

            } else if(typeID == 1) {
                // product of the sub-packets 
                result = 1;
                for (Packet packet : packets)
                    result *= packet.getValue();

            } else if(typeID == 2) {
                // minimum
                result = Long.MAX_VALUE;
                for (Packet packet : packets)
                    result = Math.min(result, packet.getValue());

            } else if(typeID == 3) {
                // maximum
                result = Long.MIN_VALUE;
                for (Packet packet : packets)
                    result = Math.max(result, packet.getValue());

            } else if(typeID == 5) {
                // greater than
                if(packets.size() != 2) {
                    logger.printf(Level.FATAL, "The packet hasn't 2 sub-packets to compare! (%d indeed)", packets.size());
                    return -1;
                }

                return (packets.get(0).getValue() > packets.get(1).getValue()) ? 1 : 0;

            } else if(typeID == 6) {
                // less than
                if(packets.size() != 2) {
                    logger.printf(Level.FATAL, "The packet hasn't 2 sub-packets to compare! (%d indeed)", packets.size());
                    return -1;
                }

                return (packets.get(0).getValue() < packets.get(1).getValue()) ? 1 : 0;

            } else if(typeID == 7) {
                // equal to
                if(packets.size() != 2) {
                    logger.printf(Level.FATAL, "The packet hasn't 2 sub-packets to compare! (%d indeed)", packets.size());
                    return -1;
                }

                return (packets.get(0).getValue() == packets.get(1).getValue()) ? 1 : 0;
            }

            // Literal values
            return (typeID == 4) ? sum : result;
        }
    }

}
