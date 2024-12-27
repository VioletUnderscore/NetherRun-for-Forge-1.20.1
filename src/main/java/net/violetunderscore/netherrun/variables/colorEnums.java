package net.violetunderscore.netherrun.variables;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class colorEnums {
    public enum NetherRunColors {
        ERR("error", 0),
        RED("red", 1),
        ORANGE("orange", 2),
        YELLOW("yellow", 3),
        LIME("lime", 4),
        AQUA("aqua", 5),
        BLUE("blue", 6),
        PURPLE("purple", 7),
        PINK("pink", 8),
        WHITE("white", 9),
        GRAY("gray", 10),
        BLACK("black", 11),
        GRADIENT_VIOLET("gradient_violet", 12),
        GRADIENT_WHITE("gradient_white", 13),
        GRADIENT_BLUE("gradient_blue", 14);

        private final String name;
        private final int id;

        NetherRunColors(String name, int id) {
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }

        public static String getNameById(int id) {
            for (NetherRunColors color : NetherRunColors.values()) {
                if (color.getId() == id) {
                    return color.getName();
                }
            }
            return null;
        }
    }
    public static Item NetherRunConcreteColor(int colorID) {
        Item[] concretes = {
                Items.WHITE_CONCRETE,
                Items.RED_CONCRETE,
                Items.ORANGE_CONCRETE,
                Items.YELLOW_CONCRETE,
                Items.LIME_CONCRETE,
                Items.LIGHT_BLUE_CONCRETE,
                Items.BLUE_CONCRETE,
                Items.PURPLE_CONCRETE,
                Items.PINK_CONCRETE,
                Items.WHITE_CONCRETE,
                Items.GRAY_CONCRETE,
                Items.BLACK_CONCRETE,
                Items.MAGENTA_CONCRETE,
                Items.WHITE_CONCRETE,
                Items.BLUE_CONCRETE
        };
        return concretes[colorID];
    }
    public static int NetherRunHexColor(int colorID) {
        int[] concretes = {
                0x000000,
                0xFF3333,
                0xFF9933,
                0xFFFF33,
                0x55FF33,
                0x33C5FF,
                0x335FFF,
                0xB133FF,
                0xFF33D6,
                0xFFFFFF,
                0x707070,
                0x595959,
                0xFF0089,
                0xFFFFFF,
                0x0000FF
        };
        return concretes[colorID];
    }
}
