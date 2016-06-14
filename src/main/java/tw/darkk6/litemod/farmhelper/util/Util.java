package tw.darkk6.litemod.farmhelper.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public class Util {
	public static boolean isDistLessThan(EntityPlayer player,BlockPos pos,int maxDistance){
		double max = maxDistance * maxDistance;
		return (player.getDistanceSqToCenter(pos)<=max);
	}
}
