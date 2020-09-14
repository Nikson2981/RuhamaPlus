package blu3.ruhamaplus.event.events;

import blu3.ruhamaplus.event.Event;
import com.jcraft.jogg.Packet;

public class PacketEvent extends Event {
    private final Packet packet;

    public PacketEvent(Packet packet) {
        super();
        this.packet = packet;
    }

    public Packet getPacket() {
        return packet;
    }

    public static class Receive extends PacketEvent {
        public Receive(Packet packet) {
            super(packet);
        }
    }

    public static class Send extends PacketEvent {
        public Send(Packet packet) {
            super(packet);
        }
    }
}
