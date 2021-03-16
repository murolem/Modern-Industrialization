/*
 * MIT License
 *
 * Copyright (c) 2020 Azercoco & Technici4n
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package aztech.modern_industrialization.machinesv2.components;

import aztech.modern_industrialization.ModernIndustrialization;
import aztech.modern_industrialization.machinesv2.IComponent;
import aztech.modern_industrialization.machinesv2.models.MachineModelClientData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Direction;

public class OrientationComponent implements IComponent {
    public Direction facingDirection = Direction.NORTH;
    public Direction outputDirection = null;
    public boolean extractItems = false;
    public boolean extractFluids = false;
    public final Params params;

    public OrientationComponent(Params params) {
        this.params = params;
        if (params.hasOutput) {
            outputDirection = Direction.NORTH;
        }
    }

    public void readNbt(CompoundTag tag) {
        facingDirection = Direction.byId(tag.getInt("facingDirection"));
        if (params.hasOutput) {
            outputDirection = Direction.byId(tag.getInt("outputDirection"));
        }
        extractItems = tag.getBoolean("extractItems");
        extractFluids = tag.getBoolean("extractFluids");
    }

    public void writeNbt(CompoundTag tag) {
        tag.putInt("facingDirection", facingDirection.getId());
        if (params.hasOutput) {
            tag.putInt("outputDirection", outputDirection.getId());
            tag.putBoolean("extractItems", extractItems);
            tag.putBoolean("extractFluids", extractFluids);
        }
    }

    public void writeModelData(MachineModelClientData data) {
        data.frontDirection = facingDirection;
        if (params.hasOutput) {
            data.outputDirection = outputDirection;
            data.itemAutoExtract = extractItems;
            data.fluidAutoExtract = extractFluids;
        }
    }

    /**
     * Try to rotate the machine, and return true if something was rotated.
     */
    public boolean onUse(PlayerEntity player, Hand hand, Direction face) {
        if (player.getStackInHand(hand).getItem().isIn(ModernIndustrialization.WRENCHES)) {
            if (player.isSneaking()) {
                if (params.hasOutput) {
                    outputDirection = face;
                    return true;
                }
            } else {
                if (face.getAxis().isHorizontal()) {
                    facingDirection = face;
                }
                // We consume the event to prevent the GUI from opening.
                return true;
            }
        }
        return false;
    }

    public void onPlaced(LivingEntity placer, ItemStack itemStack) {
        Direction dir = placer.getHorizontalFacing();
        facingDirection = dir.getOpposite();
        if (params.hasOutput) {
            outputDirection = dir;
        }
    }

    public static class Params {
        public final boolean hasOutput;
        public final boolean hasExtractItems;
        public final boolean hasExtractFluids;

        public Params(boolean hasOutput, boolean hasExtractItems, boolean hasExtractFluids) {
            this.hasOutput = hasOutput;
            this.hasExtractItems = hasExtractItems;
            this.hasExtractFluids = hasExtractFluids;
        }
    }

}
