package com.assanguino.adventofcode2021;

import org.apache.logging.log4j.Level;

public class PacketDecoder implements Executable {
    
    protected final int length_version = 3;
    protected final int length_typeID = 3;
    protected final int length_literal_value = 5;

    protected Part part;
    protected String hexadecimal;
    protected String processing_row = "";
    protected long totalSumVersion = -1;

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
        totalSumVersion = 0;
        processPacket(0);
    }

    public String printDescription() {
        return (part == Part.first) ? 
            "Packet Decoder - What do you get if you add up the version numbers in all packets ?" : 
            "Packet Decoder - ?";
    }

    public void printResult() {
        System.out.println();
        System.out.println("The version total sum is " + totalSumVersion);
        System.out.println();
    }

    public String getResult() {
        return String.valueOf(totalSumVersion);
    }

    protected Long getVersion(String string) {
        return DecodeUtils.binaryToLong(string, 0, length_version);
    }

    protected Long getTypeID(String string) {
        return DecodeUtils.binaryToLong(string, 3, length_typeID);
    }

    protected Integer processPacket(Integer index) {

        String string = processing_row.substring(index);

        long version = getVersion(string);
        long typeID = getTypeID(string);

        totalSumVersion += version;

        String typeDescription = typeID == 4 ? "literal value" : "operator packet";
        logger.printf(Level.INFO, "processing packet - version %d, typeID %d (%s) - index %d - processing from index %d", 
            version, typeID, typeDescription, index, index + length_version + length_typeID);

        Integer result;
        if(typeID == 4) {
            // literal value
            result = processLiteralValue(index + length_version + length_typeID);
        } else {
            // operator
            result = processOperator(index + length_version + length_typeID);
        }

        return result;
    }

    protected Integer processLiteralValue(Integer index) {
        Integer init_index = index;
        String sub = "", binaryResult = "";

        // TODO ensure the zero padding at the end
        /*
        String tail = string.substring(string.length() / 4);
        if(tail.length() != string.length() % 4 && !tail.contains("0")) {
            logger.printf(Level.ERROR, "     processing literal value tail [%s]", tail);
        }
        */

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

        return index;
    }

    protected Integer processOperator(Integer index) {

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
                index = processPacket(index);
            }

        } else {
            noSubPackets = DecodeUtils.binaryToLong(processing_row, index, 11);
            index += 11;

            logger.printf(Level.INFO, "     process operator labeled I = '1' - total amount of sub-packets %d - index %d", noSubPackets, index);

            while(noSubPackets > 0) {
                logger.printf(Level.DEBUG, "     * process operator packet, from index %d - sub-packets to be processed: %d", index, noSubPackets);
                index = processPacket(index);
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

}
