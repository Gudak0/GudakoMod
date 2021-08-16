package gudako.mod.items;

import gudako.mod.GudakoMod;
import net.minecraft.item.Item;

public class SuperGudakium extends Item
{

	public SuperGudakium(Properties props) 
	{
		super(props);
	}
	
	@Override
	public boolean isFireResistant() 
	{
		return true;
	}
	
	public boolean isFoil() 
	{
		return true;
	}
	
}
