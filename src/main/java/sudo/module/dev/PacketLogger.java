package sudo.module.dev;

import sudo.module.Mod;

public class PacketLogger extends Mod {

    public static PacketLogger get;

    public PacketLogger() {
        super("PacketLogger", "description", Category.WORLD, 0);
        get = this;
    }
}
