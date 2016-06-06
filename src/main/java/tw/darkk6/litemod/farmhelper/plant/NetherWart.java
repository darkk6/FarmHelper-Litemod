package tw.darkk6.litemod.farmhelper.plant;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockSoulSand;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NetherWart extends IPlants {

	@Override
	public Item getItem() {
		return Items.NETHER_WART;
	}

	@Override
	public boolean canPlantAt(World world, BlockPos pos) {
		Block block=world.getBlockState(pos).getBlock();
		Block blockUp=world.getBlockState(pos.up()).getBlock();
		if( (block instanceof BlockSoulSand) && (blockUp instanceof BlockAir) ){
			return true;
		}
		return false;
	}

}
