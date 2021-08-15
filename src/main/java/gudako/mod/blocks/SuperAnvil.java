package gudako.mod.blocks;

import gudako.mod.container.SuperAnvilContainer;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class SuperAnvil extends Block {
    private static final ITextComponent CONTAINER_TITLE = new TranslationTextComponent("gudako.repair");

    public SuperAnvil()
    {
        super(Properties.of(Material.METAL).sound(SoundType.METAL).strength(2.0F));
    }

    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult brtr) {
        if (world.isClientSide) {
            return ActionResultType.SUCCESS;
        } else {
            player.openMenu(state.getMenuProvider(world, pos));
            player.awardStat(Stats.INTERACT_WITH_ANVIL);
            return ActionResultType.CONSUME;
        }
    }

    @Nullable
    public INamedContainerProvider getMenuProvider(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedContainerProvider((p_220272_2_, p_220272_3_, p_220272_4_) -> {
            return new SuperAnvilContainer(p_220272_2_, p_220272_3_, IWorldPosCallable.create(world, pos));
        }, CONTAINER_TITLE);
    }
}

