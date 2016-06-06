package tw.darkk6.litemod.farmhelper.plant;

import java.util.Vector;

import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class IPlants {
	
	public abstract Item getItem();
	public abstract boolean canPlantAt(World world,BlockPos pos);
	
	public BlockPos[] getOrderPosForBFS(BlockPos pos,boolean faster){
		BlockPos n=pos.north();
		BlockPos s=pos.south();
		BlockPos w=pos.west();
		BlockPos e=pos.east();
		if(faster){ return new BlockPos[]{n,e,w,s};}
		else{
			return new BlockPos[]{
					n , n.east() ,
					e , e.south() ,
					s , s.west() ,
					w , w.north()
			};
		}
	}
	
	public Vector<BlockPos> doBFSSearch(World world,BlockPos pos,int count,boolean faster){
		Vector<BlockPos> list=new Vector<BlockPos>();
		list.addElement(pos);
		//開始 BFS 搜尋
		for(int idx=0; idx<list.size() && list.size()<=count ;idx++){
			BlockPos[] arr=getOrderPosForBFS(list.get(idx),faster);
			for(int i=0;i<arr.length && list.size()<=count;i++){
				if( !(list.contains(arr[i])) && canPlantAt(world,arr[i]))
					list.addElement(arr[i]);
			}
		}
		return list;
	}
}
