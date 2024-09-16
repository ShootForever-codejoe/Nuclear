package com.shootforever.nuclear.util.wrapper;

import com.shootforever.nuclear.Nuclear;
import net.minecraft.client.Minecraft;


public interface Wrapper {
    Minecraft mc = Nuclear.INSTANCE.getMinecraft();
}
