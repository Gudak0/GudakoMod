package gudako.mod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class GudakiumOre extends Block
{

	public GudakiumOre() {
		super(Properties.of(Material.STONE).sound(SoundType.STONE).strength(2.0F));
	}

}
