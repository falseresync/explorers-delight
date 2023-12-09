package dev.falseresync.exdel.datagen;

import dev.falseresync.exdel.ExDel;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;

public class ExDelRecipeProvider extends FabricRecipeProvider {
    public ExDelRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ExDel.OWNERIZER)
                .input('m', ExDel.MYSTERY)
                .input('s', Items.STICK)
                .pattern("m ")
                .pattern(" s")
                .criterion("has_mystery", conditionsFromItem(ExDel.MYSTERY))
                .offerTo(exporter);
    }
}
