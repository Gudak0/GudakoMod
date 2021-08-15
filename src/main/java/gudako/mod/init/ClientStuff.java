package gudako.mod.init;

import gudako.mod.gui.SuperAnvilScreen;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(
        modid = "gudako",
        value = {Dist.CLIENT},
        bus = Bus.MOD
)
public class ClientStuff {
    public ClientStuff() {
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static void init(FMLClientSetupEvent event) {
        ScreenManager.register((ContainerType)Registration.SUPERANVIL_CONTAINER.get(), SuperAnvilScreen::new);
    }
}
