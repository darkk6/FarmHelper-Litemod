package tw.darkk6.litemod.farmhelper.plant;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockFarmland;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class FarmlandPlantBase extends IPlants {
	
	/* 
	 * TODO 之後可以改成用 Block.canSustainPlant + 上方是否為空氣
	 * worldIn.getBlockState(pos);
	*/
	
	@Override
	public boolean canPlantAt(World world, BlockPos pos) {
		Block block=world.getBlockState(pos).getBlock();
		Block blockUp=world.getBlockState(pos.up()).getBlock();
		if( (block instanceof BlockFarmland) && (blockUp instanceof BlockAir) ){
			return true;
		}
		return false;
	}

}
