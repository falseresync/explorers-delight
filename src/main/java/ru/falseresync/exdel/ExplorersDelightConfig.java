package ru.falseresync.exdel;


//@Syncing
public class ExplorersDelightConfig { // implements Config {
    public MysteryArrowConfig mysteryArrow = new MysteryArrowConfig();

//    @Override
    public String getName() {
        return "exdel";
    }

//    @Override
    public String getModid() {
        return "exdel";
    }

    public static class MysteryArrowConfig {
        public float agingChance = 1.0F;
        public float transformationChance = 0.75F;
    }
}
