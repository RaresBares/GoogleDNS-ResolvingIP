import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class DNSClient {
    private static final String DNS_SERVER_ADDRESS = "8.8.8.8";
    private static final int DNS_SERVER_PORT = 53;

    public static void main(String[] args) throws IOException {
        String domain = "google.com";
        InetAddress ipAddress = InetAddress.getByName(DNS_SERVER_ADDRESS);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);




        dos.writeShort(0x1234); // 4660

        // Write Query Flags
        dos.writeShort(0x0001);
        // Question Count
        dos.writeShort(0x0001);
        // Answer Record Count
        dos.writeShort(0x0000);
        // Authority Record Count
        dos.writeShort(0x0000);
        // Additional Record Count
        dos.writeShort(0x0000);

        String[] domainParts = domain.split("\\.");
        System.out.println(domain + " has " + domainParts.length + " parts");

        for (int i = 0; i<domainParts.length; i++) {
            System.out.println("Writing: " + domainParts[i]);
            byte[] domainBytes = domainParts[i].getBytes("UTF-8");
            dos.writeByte(domainBytes.length);
            dos.write(domainBytes);
        }

        // null byte für ende
        dos.writeByte(0x00);

        // Type 0x01 = A (Host Request) sheesh
        dos.writeShort(0x0001);

        // Class 0x01 = IN ttt
        dos.writeShort(0x0001);

        byte[] dnsFrame = baos.toByteArray();

        System.out.println("Sending: " + dnsFrame.length + " bytes");
        for (int i =0; i< dnsFrame.length; i++) {
            System.out.print("0x" + String.format("%x", dnsFrame[i]) + " " );
        }


        DatagramSocket socket = new DatagramSocket();
        DatagramPacket dnsReqPacket = new DatagramPacket(dnsFrame, dnsFrame.length, ipAddress, DNS_SERVER_PORT);
        socket.send(dnsReqPacket);


        byte[] buf = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        DataInputStream din = new DataInputStream(new ByteArrayInputStream(buf));
        System.out.println("\n\nReceived: " + packet.getLength() + " bytes");
        System.out.println("\n\n MSG: \n");
        for (int i = 0; i < packet.getLength(); i++) {
            byte b = din.readByte();
            System.out.println(toBits(b) + " | " + b + "[" + (char) b + "]");

        }
        /*for (int i = 0; i < packet.getLength(); i++) {
            System.out.print(" 0x" + String.format("%x", buf[i]) + " " );
        }
        System.out.println("\n");



        System.out.println("Transaction ID: 0x" + String.format("%x", din.readShort()));
        System.out.println("Flags: 0x" + String.format("%x", din.readShort()));
        System.out.println("Questions: 0x" + String.format("%x", din.readShort()));
        System.out.println("Answers RRs: 0x" + String.format("%x", din.readShort()));
        System.out.println("Authority RRs: 0x" + String.format("%x", din.readShort()));
        System.out.println("Additional RRs: 0x" + String.format("%x", din.readShort()));

        int recLen = 0;
        while ((recLen = din.readByte()) > 0) {
            byte[] record = new byte[recLen];

            for (int i = 0; i < recLen; i++) {
                record[i] = din.readByte();
            }

            System.out.println("Record: " + new String(record, "UTF-8"));
        }

        System.out.println("Record Type: 0x" + String.format("%x", din.readShort()));
        System.out.println("Class: 0x" + String.format("%x", din.readShort()));

        System.out.println("Field: 0x" + String.format("%x", din.readShort()));
        System.out.println("Type: 0x" + String.format("%x", din.readShort()));
        System.out.println("Class: 0x" + String.format("%x", din.readShort()));
        System.out.println("TTL: 0x" + String.format("%x", din.readInt()));

        short addrLen = din.readShort();
        System.out.println("Len: 0x" + String.format("%x", addrLen));

        System.out.print("Address: ");
        for (int i = 0; i < addrLen; i++ ) {
            System.out.print("" + String.format("%d", (din.readByte() & 0xFF)) + ".");


        }*/
    }
    public  static String toBits(final byte val) {
        final StringBuilder result = new StringBuilder();
        for (int i=0; i<8; i++) {
            result. append((int)(val >> (8-(i+1)) & 0x0001));
        }
        return result. toString();
    }

}