package net.nitwit.guinea_pigs;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.nitwit.guinea_pigs.datagen.ModModelProvider;

public class GuineaPigsDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		// Create a data pack where you can add your data providers
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		// Register your custom model provider to generate model JSON files
		pack.addProvider(ModModelProvider::new);
	}
}
