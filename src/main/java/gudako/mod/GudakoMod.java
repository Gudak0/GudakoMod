package gudako.mod;

import gudako.mod.init.ClientStuff;
import gudako.mod.init.Registration;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(GudakoMod.ModID)
public class GudakoMod
{
    @SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger();
    public static final String ModID = "gudako";
    public static final ItemGroup MOD_ITEMS = new ModGroup("gudakomoditems");

    public GudakoMod()
    {
        Registration.Init();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientStuff::init);
    }
    public static class ModGroup extends ItemGroup
    {

        public ModGroup(String label)
        {
            super(label);
        }

        @Override
        public ItemStack makeIcon()
        {
            return Registration.SUPERANVIL_ITEM.get().getDefaultInstance();
        }
    }
}
