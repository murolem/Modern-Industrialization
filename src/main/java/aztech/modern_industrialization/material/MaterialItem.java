package aztech.modern_industrialization.material;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class MaterialItem extends Item {

    private String materialId, itemType;

    public MaterialItem(Settings settings, String materialId, String itemType) {
        super(settings);
        this.materialId = materialId;
        this.itemType = itemType;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public Text getName() {
        String itemTypeKey = "modern_industrialization:item_type:"+itemType;
        String materialKey = "modern_industrialization:material:"+materialId;
        return new TranslatableText(materialKey).append(" ").append(new TranslatableText(itemTypeKey));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public Text getName(ItemStack stack) {
        return this.getName();
    }



}
