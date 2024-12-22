package flooferland.showbiz.datagen;

import flooferland.showbiz.backend.util.ShowbizEnv;
import flooferland.showbiz.datagen.providers.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class ShowbizDataGenerator implements DataGeneratorEntrypoint {
	public static boolean runningDatagen = false;
	
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		FabricDataGenerator.Pack pack = generator.createPack();
		pack.addProvider(ModBlockTagProvider::new);
		pack.addProvider(ModItemTagProvider::new);
		pack.addProvider(ModLootTableProvider::new);
		pack.addProvider(ModRecipeProvider::new);
		pack.addProvider(ModSoundProvider::new);
		if (!ShowbizEnv.isServer()) {
			pack.addProvider(ModModelProvider::new);
		}
	}
}
