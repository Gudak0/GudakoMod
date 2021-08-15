package gudako.mod.container;

import gudako.mod.blocks.SuperAnvil;
import gudako.mod.init.Registration;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Nonnull;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.AbstractRepairContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SuperAnvilContainer extends AbstractRepairContainer {
    @SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger();
    public int repairItemCountCost;
    private String itemName;
    public SuperAnvil sanvil;
    private final IntReferenceHolder cost;

    public SuperAnvilContainer(int p_i50101_1_, PlayerInventory p_i50101_2_) {
        this(p_i50101_1_, p_i50101_2_, IWorldPosCallable.NULL);
    }

    @SuppressWarnings("rawtypes")
	public SuperAnvilContainer(int p_i50102_1_, PlayerInventory p_i50102_2_, IWorldPosCallable p_i50102_3_) {
        super((ContainerType)Registration.SUPERANVIL_CONTAINER.get(), p_i50102_1_, p_i50102_2_, p_i50102_3_);
        this.cost = IntReferenceHolder.standalone();
        this.addDataSlot(this.cost);
    }

    protected boolean isValidBlock(BlockState state) {
        return true;
    }

    public boolean onAnvilChange(SuperAnvilContainer container, @Nonnull ItemStack left, @Nonnull ItemStack right, IInventory outputSlot, String name, int baseCost, PlayerEntity player) {
        AnvilUpdateEvent e = new AnvilUpdateEvent(left, right, name, baseCost, player);
        if (MinecraftForge.EVENT_BUS.post(e)) {
            return false;
        } else if (e.getOutput().isEmpty()) {
            return true;
        } else {
            outputSlot.setItem(0, e.getOutput());
            container.setMaximumCost(e.getCost());
            container.repairItemCountCost = e.getMaterialCost();
            return false;
        }
    }

    protected boolean mayPickup(PlayerEntity p_230303_1_, boolean p_230303_2_) {
        return p_230303_1_.experienceLevel >= this.cost.get() && this.cost.get() > 0;
    }

    protected ItemStack onTake(PlayerEntity p_230301_1_, ItemStack p_230301_2_) {
        if (!p_230301_1_.abilities.instabuild) {
            p_230301_1_.giveExperienceLevels(-this.cost.get());
        }

        this.inputSlots.setItem(0, ItemStack.EMPTY);
        if (this.repairItemCountCost > 0) {
            ItemStack itemstack = this.inputSlots.getItem(1);
            if (!itemstack.isEmpty() && itemstack.getCount() > this.repairItemCountCost) {
                itemstack.shrink(this.repairItemCountCost);
                this.inputSlots.setItem(1, itemstack);
            } else {
                this.inputSlots.setItem(1, ItemStack.EMPTY);
            }
        } else {
            this.inputSlots.setItem(1, ItemStack.EMPTY);
        }

        this.cost.set(0);
        this.access.execute((p_234633_1_, p_234633_2_) -> {
            BlockState blockstate = p_234633_1_.getBlockState(p_234633_2_);
            if (blockstate.is(BlockTags.ANVIL)) {
                BlockState blockstate1 = AnvilBlock.damage(blockstate);
                if (blockstate1 == null) {
                    p_234633_1_.removeBlock(p_234633_2_, false);
                    p_234633_1_.levelEvent(1029, p_234633_2_, 0);
                } else {
                    p_234633_1_.setBlock(p_234633_2_, blockstate1, 2);
                    p_234633_1_.levelEvent(1030, p_234633_2_, 0);
                }
            } else {
                p_234633_1_.levelEvent(1030, p_234633_2_, 0);
            }

        });
        return p_230301_2_;
    }

    public void createResult() {
        ItemStack itemstack = this.inputSlots.getItem(0);
        this.cost.set(1);
        int i = 0;
        int t = 0;
        int k = 0;
        if (itemstack.isEmpty()) {
            this.resultSlots.setItem(0, ItemStack.EMPTY);
            this.cost.set(0);
        } else {
            ItemStack itemstack1 = itemstack.copy();
            ItemStack itemstack2 = this.inputSlots.getItem(1);
            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemstack1);
            int j = t + itemstack.getBaseRepairCost() + (itemstack2.isEmpty() ? 0 : itemstack2.getBaseRepairCost());
            this.repairItemCountCost = 0;
            boolean flag = false;
            int k2;
            if (!itemstack2.isEmpty()) {
                if (!this.onAnvilChange(this, itemstack, itemstack2, this.resultSlots, this.itemName, j, this.player)) {
                    return;
                }

                flag = itemstack2.getItem() == Items.ENCHANTED_BOOK && !EnchantedBookItem.getEnchantments(itemstack2).isEmpty();
                int i1;
                int j1;
                if (itemstack1.isDamageableItem() && itemstack1.getItem().isValidRepairItem(itemstack, itemstack2)) {
                    k2 = Math.min(itemstack1.getDamageValue(), itemstack1.getMaxDamage() / 4);
                    if (k2 <= 0) {
                        this.resultSlots.setItem(0, ItemStack.EMPTY);
                        this.cost.set(0);
                        return;
                    }

                    for(i1 = 0; k2 > 0 && i1 < itemstack2.getCount(); ++i1) {
                        j1 = itemstack1.getDamageValue() - k2;
                        itemstack1.setDamageValue(j1);
                        ++i;
                        k2 = Math.min(itemstack1.getDamageValue(), itemstack1.getMaxDamage() / 4);
                    }

                    this.repairItemCountCost = i1;
                } else {
                    if (!flag && (itemstack1.getItem() != itemstack2.getItem() || !itemstack1.isDamageableItem())) {
                        this.resultSlots.setItem(0, ItemStack.EMPTY);
                        this.cost.set(0);
                        return;
                    }

                    if (itemstack1.isDamageableItem() && !flag) {
                        k2 = itemstack.getMaxDamage() - itemstack.getDamageValue();
                        i1 = itemstack2.getMaxDamage() - itemstack2.getDamageValue();
                        j1 = i1 + itemstack1.getMaxDamage() * 12 / 100;
                        int k1 = k2 + j1;
                        int l1 = itemstack1.getMaxDamage() - k1;
                        if (l1 < 0) {
                            l1 = 0;
                        }

                        if (l1 < itemstack1.getDamageValue()) {
                            itemstack1.setDamageValue(l1);
                            i += 2;
                        }
                    }

                    Map<Enchantment, Integer> map1 = EnchantmentHelper.getEnchantments(itemstack2);
                    boolean flag2 = false;
                    boolean flag3 = false;
                    @SuppressWarnings("rawtypes")
					Iterator var22 = map1.keySet().iterator();

                    label152:
                    while(true) {
                        Enchantment enchantment1;
                        do {
                            if (!var22.hasNext()) {
                                if (flag3 && !flag2) {
                                    this.resultSlots.setItem(0, ItemStack.EMPTY);
                                    this.cost.set(0);
                                    return;
                                }
                                break label152;
                            }

                            enchantment1 = (Enchantment)var22.next();
                        } while(enchantment1 == null);

                        int i2 = (Integer)map.getOrDefault(enchantment1, 0);
                        int j2 = (Integer)map1.get(enchantment1);
                        j2 = i2 == j2 ? j2 + 1 : Math.max(j2, i2);
                        boolean flag1 = enchantment1.canEnchant(itemstack);
                        if (this.player.abilities.instabuild || !this.player.abilities.instabuild || itemstack.getItem() == Items.ENCHANTED_BOOK) {
                            flag1 = true;
                        }

                        if (!flag1) {
                            flag3 = true;
                        } else {
                            flag2 = true;
                            map.put(enchantment1, j2);
                            int k3 = 0;
                            switch(enchantment1.getRarity()) {
                                case COMMON:
                                    k3 = 1;
                                    break;
                                case UNCOMMON:
                                    k3 = 2;
                                    break;
                                case RARE:
                                    k3 = 4;
                                    break;
                                case VERY_RARE:
                                    k3 = 8;
                            }

                            if (flag) {
                                k3 = Math.max(1, k3 / 2);
                            }

                            i += k3 * j2;
                            if (itemstack.getCount() > 1) {
                                i = 40;
                            }
                        }
                    }
                }
            }

            if (StringUtils.isBlank(this.itemName)) {
                if (itemstack.hasCustomHoverName()) {
                    k = 1;
                    i += k;
                    itemstack1.resetHoverName();
                }
            } else if (!this.itemName.equals(itemstack.getHoverName().getString())) {
                k = 1;
                i += k;
                itemstack1.setHoverName(new StringTextComponent(this.itemName));
            }

            if (flag && !itemstack1.isBookEnchantable(itemstack2)) {
                itemstack1 = ItemStack.EMPTY;
            }

            this.cost.set(j + i);
            if (i <= 0) {
                itemstack1 = ItemStack.EMPTY;
            }

            if (k == i && k > 0 && this.cost.get() >= 2147483647) {
                this.cost.set(2147483646);
            }

            if (this.cost.get() >= 2147483647 && !this.player.abilities.instabuild) {
                itemstack1 = ItemStack.EMPTY;
            }

            if (!itemstack1.isEmpty()) {
                k2 = itemstack1.getBaseRepairCost();
                if (!itemstack2.isEmpty() && k2 < itemstack2.getBaseRepairCost()) {
                    k2 = itemstack2.getBaseRepairCost();
                }

                if (k != i || k == 0) {
                    k2 = calculateIncreasedRepairCost(k2);
                }

                itemstack1.setRepairCost(k2);
                EnchantmentHelper.setEnchantments(map, itemstack1);
            }

            this.resultSlots.setItem(0, itemstack1);
            this.broadcastChanges();
        }

    }

    public static int calculateIncreasedRepairCost(int p_216977_0_) {
        return p_216977_0_ * 2 + 1;
    }

    public void setItemName(String p_82850_1_) {
        this.itemName = p_82850_1_;
        if (this.getSlot(2).hasItem()) {
            ItemStack itemstack = this.getSlot(2).getItem();
            if (StringUtils.isBlank(p_82850_1_)) {
                itemstack.resetHoverName();
            } else {
                itemstack.setHoverName(new StringTextComponent(this.itemName));
            }
        }

        this.createResult();
    }

    @OnlyIn(Dist.CLIENT)
    public int getCost() {
        return this.cost.get();
    }

    public void setMaximumCost(int value) {
        this.cost.set(value);
    }
}

