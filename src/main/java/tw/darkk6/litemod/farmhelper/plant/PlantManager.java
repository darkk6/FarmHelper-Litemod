package tw.darkk6.litemod.farmhelper.plant;

import java.util.HashMap;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tw.darkk6.litemod.farmhelper.util.Config;
import tw.darkk6.litemod.farmhelper.util.ItemRef;

public class PlantManager {
//============= 以後有新增植物要寫在這裡 =============
	public static HashMap<Item,IPlants> immutableMap;
	public static HashMap<Item,IPlants> map;
	static{
		immutableMap=new HashMap<Item, IPlants>();
		immutableMap.put(ItemRef.BEETROOT_SEEDS, new BeetrootSeed());
		immutableMap.put(ItemRef.CARROT, new Carrot());
		immutableMap.put(ItemRef.MELON_SEEDS, new MelonSeed());
		immutableMap.put(ItemRef.NETHER_WART, new NetherWart());
		immutableMap.put(ItemRef.POTATO, new Potato());
		immutableMap.put(ItemRef.PUMPKIN_SEEDS, new PumpkinSeed());
		immutableMap.put(ItemRef.WHEAT_SEEDS, new WheatSeed());
	}
	public static void update(){
		if(map==null) map=new HashMap<Item, IPlants>();
		else map.clear();
		
		if(Config.get().beetroot)
			map.put(ItemRef.BEETROOT_SEEDS, immutableMap.get(ItemRef.BEETROOT_SEEDS));
		if(Config.get().carrot)
			map.put(ItemRef.CARROT, immutableMap.get(ItemRef.CARROT));
		if(Config.get().melon)
			map.put(ItemRef.MELON_SEEDS, immutableMap.get(ItemRef.MELON_SEEDS));
		if(Config.get().wart)
			map.put(ItemRef.NETHER_WART, immutableMap.get(ItemRef.NETHER_WART));
		if(Config.get().potato)
			map.put(ItemRef.POTATO, immutableMap.get(ItemRef.POTATO));
		if(Config.get().pumpkin)
			map.put(ItemRef.PUMPKIN_SEEDS, immutableMap.get(ItemRef.PUMPKIN_SEEDS));
		if(Config.get().wheat)
			map.put(ItemRef.WHEAT_SEEDS, immutableMap.get(ItemRef.WHEAT_SEEDS));
	}
	
	public static IPlants get(Item item){
		return map.get(item);
	}
	
	// 該物品是否為此 Mod 可用之種植物(不一定有啟用)
	public static boolean isPlantableCrop(ItemStack stack){
		if(stack==null) return false;
		return immutableMap.containsKey(stack.getItem());
	}
	
	// 取得該物品是否在設定檔中有開啟幫忙種植
	public static boolean isPlantEnabled(ItemStack stack){
		if(stack==null) return false;
		return map.containsKey(stack.getItem());
	}
	// 取得該物品是否可以種植，這裡不論設定檔是否有啟用
	public static boolean canPlantAt(World world,BlockPos pos,ItemStack stack){
		if(stack==null) return false;
		if(!immutableMap.containsKey(stack.getItem())) return false;
		IPlants plant=immutableMap.get(stack.getItem());
		return plant.canPlantAt(world, pos);
	}
}
