package me.falsehonesty.asmhelpermod;

import me.falsehonesty.asmhelper.example.BasicKt;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = "asmhelpermod", name = "AsmHelperMod", version = "1.0")
public class AsmHelperMod {
    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        BasicKt.test();
    }
}
