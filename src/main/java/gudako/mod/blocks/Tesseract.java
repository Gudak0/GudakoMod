package gudako.mod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class Tesseract extends Block
{
    public Tesseract()
    {
        super(Properties.of(Material.METAL).sound(SoundType.METAL).strength(2.0F));
    }
}
