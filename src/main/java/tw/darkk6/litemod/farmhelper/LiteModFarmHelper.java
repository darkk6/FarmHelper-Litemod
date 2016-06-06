package tw.darkk6.litemod.farmhelper;

import java.io.File;
import java.util.Vector;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import tw.darkk6.litemod.farmhelper.plant.IPlants;
import tw.darkk6.litemod.farmhelper.plant.PlantManager;
import tw.darkk6.litemod.farmhelper.util.Config;
import tw.darkk6.litemod.farmhelper.util.Log;
import tw.darkk6.litemod.farmhelper.util.Reference;

import com.mumfrey.liteloader.Configurable;
import com.mumfrey.liteloader.PlayerClickListener;
import com.mumfrey.liteloader.PlayerInteractionListener.MouseButton;
import com.mumfrey.liteloader.modconfig.ConfigPanel;

// NOTE PlayerInteractListener 是 Server Side
public class LiteModFarmHelper implements PlayerClickListener,Configurable {

	public LiteModFarmHelper(){}
	
	@Override
	public void init(File configPath) {
		Config.get().init();
		PlantManager.update();
	}
	@Override
	public String getVersion() { return Reference.MOD_VER; }
	@Override
	public void upgradeSettings(String version, File configPath, File oldConfigPath) {}
	@Override
	public String getName() { return Reference.MOD_NAME; }
	@Override
	public boolean onMouseClicked(EntityPlayerSP player, MouseButton button) {
		if(button!=MouseButton.RIGHT) return true;
		Minecraft mc=Minecraft.getMinecraft();
		if(player==null) return true;//避免出錯，檢查一下
		//取得十字準心面對的那一格，如果沒東西，或者點選的不是 Block , 則結束
		RayTraceResult pointed=player.rayTrace(mc.playerController.getBlockReachDistance(), 1.0F);
		if( pointed==null || pointed.typeOfHit!=RayTraceResult.Type.BLOCK) return true;
		
		//應該沒問題可以觸發 onBlockRightClick 了
		this.onBlockRightClick(mc,player,pointed.getBlockPos(),pointed.sideHit);
		return true;
	}
	
	private void onBlockRightClick(Minecraft mc,EntityPlayerSP player,BlockPos hitPos,EnumFacing sideHit){
		
		// Main慣用手拿的東西 , off 副手拿的東西 - 1.9
		ItemStack stackMain=player.getHeldItem(EnumHand.MAIN_HAND);
		ItemStack stackOff=player.getHeldItem(EnumHand.OFF_HAND);
		ItemStack stack=null;//實際要種植的東西
		EnumHand whichHand=null;
		
		//1.9 - 先確認主手拿的東西是否為可種植且設定檔中有啟用 , 如果可以就讓 stack 指向 stackMain
		//      反之，就偵測副手 , 並讓 stack 指向 stackOff 
		if(PlantManager.isPlantEnabled(stackMain) && PlantManager.canPlantAt(player.worldObj, hitPos, stackMain)){
			stack=stackMain;
			whichHand=EnumHand.MAIN_HAND;
		}else if(PlantManager.isPlantEnabled(stackOff) && PlantManager.canPlantAt(player.worldObj, hitPos, stackOff)){
			stack=stackOff;
			whichHand=EnumHand.OFF_HAND;
		}else return; //主副手都不是要重的東西，就結束
		
		//當要種植副手的東西時，要檢查，主手的物品是不是只是被禁用而已
		if(whichHand==EnumHand.OFF_HAND && stackMain!=null){
			//如果 MainHand 的 Item 可以被種在這一格，就代表只是禁用而已，這時候要提示訊息
			if(PlantManager.canPlantAt(player.worldObj, hitPos, stackMain)){
				Log.showMainHandMustNull();
				return;
			}
		}
		//設定檔中指定的最大值
		int maxCount=Config.get().maxCount;
		int count=stack.stackSize;//手上道具的數量
		if(count==1 || maxCount==1) return;//如果手上道具只有一個或者最大種植量是1，就不做任何搜尋
		//調整種植量
		if(maxCount!=0) count=Math.min(count, maxCount);
		Vector<BlockPos> list=null;
		IPlants plant=PlantManager.get(stack.getItem());
		// 要先判斷該格是否能種植 , 不能種植就不繼續
		if(!plant.canPlantAt(player.worldObj, hitPos)) return;
		list = plant.doBFSSearch(player.worldObj, hitPos, count, Config.get().fasterBFS);
		
		//==== BFS 搜尋完成，開始種植 =====
		//數量內的點都找完了 , 將有效數量的種下去，跳過 0 是因為  0=作用的那一格
		for(int i=1;i<count && i<list.size();i++){
			//這裡不能使用 itemStack.onItemUse , 那個不會與 Server 通訊 , 物品只是看起來有而已
			// 1.9 , 改用 processRightClickBlock , 1.8.8=>onPlayerRightClick
			mc.playerController.processRightClickBlock(
					player,
					(WorldClient)(player.worldObj), 
					stack, 
					list.get(i),
					sideHit,
					player.getPositionVector(),
					whichHand
				);
		}
		return;
	}
	
	
	@Override
	public boolean onMouseHeld(EntityPlayerSP player, MouseButton button) { return true; }

	@Override
	public Class<? extends ConfigPanel> getConfigPanelClass(){
		return GuiFarmhelperConfig.class;
	}
}
