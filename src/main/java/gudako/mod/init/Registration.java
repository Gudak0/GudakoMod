package gudako.mod.init;

import gudako.mod.GudakoMod;
import gudako.mod.blocks.GudakiumBlock;
import gudako.mod.blocks.GudakiumOre;
import gudako.mod.blocks.SuperAnvil;
import gudako.mod.blocks.SuperGudakiumBlock;
import gudako.mod.blocks.Tesseract;
import gudako.mod.container.SuperAnvilContainer;
import gudako.mod.items.SuperGudakium;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Registration
{
	//TODO: Bed to sleep to night
	
    private static final DeferredRegister<Block> BLOCKS;
    private static final DeferredRegister<Item> ITEMS;
    private static final DeferredRegister<TileEntityType<?>> TILES;
    private static final DeferredRegister<ContainerType<?>> CONTAINERS;

    public static final RegistryObject<SuperAnvil> SUPER_ANVIL;
    public static RegistryObject<Item> SUPERANVIL_ITEM;
    public static RegistryObject<ContainerType<SuperAnvilContainer>> SUPERANVIL_CONTAINER;
    
    public static final RegistryObject<GudakiumOre> GUDAKIUM_ORE;
    public static RegistryObject<Item> GUDAKIUMORE_ITEM;

    public static RegistryObject<Item> GUDAKIUM_INGOT;
    public static final RegistryObject<GudakiumBlock> GUDAKIUM_BLOCK;
    public static RegistryObject<Item> GUDAKIUMBLOCK_ITEM;
    
    public static RegistryObject<Item> SUPER_GUDAKIUM;
    public static final RegistryObject<SuperGudakiumBlock> SUPER_GUDAKIUM_BLOCK;
    public static RegistryObject<Item> SUPER_GUDAKIUMBLOCK_ITEM;
    
    public static final RegistryObject<Tesseract> TESSERACT;
    public static RegistryObject<Item> TESSERACT_ITEM;

    public Registration() {
    }

    public static void Init() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    static
    {
        BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "gudako");
        ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "gudako");
        TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, "gudako");
        CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, "gudako");

        SUPER_ANVIL = BLOCKS.register("super_anvil", SuperAnvil::new);
        TESSERACT = BLOCKS.register("tesseract", Tesseract::new);
        SUPER_GUDAKIUM_BLOCK = BLOCKS.register("super_gudakium_block", SuperGudakiumBlock::new);
        
        GUDAKIUM_ORE = BLOCKS.register("gudakium_ore", GudakiumOre::new);
        GUDAKIUM_BLOCK = BLOCKS.register("gudakium_block", GudakiumBlock::new);
        
        registerItem(GUDAKIUM_INGOT, "gudakium_ingot");
        
        GUDAKIUM_INGOT = ITEMS.register("super_gudakium", SuperGudakium::new);
        
        registerItemBlock(TESSERACT_ITEM,"tesseract", TESSERACT);
        registerItemBlock(SUPERANVIL_ITEM, "super_anvil", SUPER_ANVIL);
        registerItemBlock(GUDAKIUMORE_ITEM, "gudakium_ore", GUDAKIUM_ORE);
        registerItemBlock(GUDAKIUMBLOCK_ITEM, "gudakium_block", GUDAKIUM_BLOCK);

        SUPERANVIL_CONTAINER = CONTAINERS.register("super_anvil", () -> {
           return IForgeContainerType.create((windowId, inv, data) -> {
                return new SuperAnvilContainer(windowId, inv);
            });
        });
    }
    private static void registerItemBlock(RegistryObject<Item> item, String name, RegistryObject<?> block)
    {
        item = ITEMS.register(name, () -> {
            return new BlockItem((Block)block.get(), new Item.Properties().tab(GudakoMod.MOD_ITEMS));
        });
    }
    
    private static void registerItem(RegistryObject<Item> item, String name)
    {
    	item = ITEMS.register(name, () ->
    	new Item(new Item.Properties().tab(GudakoMod.MOD_ITEMS)));
    }
}

