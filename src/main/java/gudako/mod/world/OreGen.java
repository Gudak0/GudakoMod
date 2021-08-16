package gudako.mod.world;

import gudako.mod.init.Registration;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;

public class OreGen 
{
    public static final ConfiguredFeature<?, ?> gudakiumOre = Feature.ORE.configured(
            new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, Registration.GUDAKIUM_ORE.get().defaultBlockState(), 4)) //vein size
            .range(12).squared() //maximum y level where ore can spawn
            .count(3); //how many veins maximum per chunk

    public static void generateOre(final BiomeLoadingEvent event) {
        BiomeGenerationSettingsBuilder generation = event.getGeneration();
        if (!(event.getCategory().equals(Biome.Category.NETHER) || event.getCategory().equals(Biome.Category.THEEND))) 
        { 
            generation.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, gudakiumOre);
        }
    }
}
