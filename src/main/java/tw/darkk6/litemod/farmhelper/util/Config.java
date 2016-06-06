package tw.darkk6.litemod.farmhelper.util;

import java.lang.reflect.Field;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mumfrey.liteloader.core.LiteLoader;
import com.mumfrey.liteloader.modconfig.ConfigStrategy;
import com.mumfrey.liteloader.modconfig.Exposable;
import com.mumfrey.liteloader.modconfig.ExposableOptions;

@ExposableOptions(strategy=ConfigStrategy.Unversioned , filename=Reference.MOD_CONFIG)
public class Config implements Exposable{
	private static Config instance=null;
	public static Config get(){
		if(instance==null) instance=new Config();
		return instance;
	}
	
	@Expose @SerializedName("fasterBFS")
	public boolean fasterBFS=false;
	
	@Expose @SerializedName("beetroot")
	public boolean beetroot=true;
	
	@Expose @SerializedName("carrot")
	public boolean carrot=true;
	
	@Expose @SerializedName("melon")
	public boolean melon=false;
	
	@Expose @SerializedName("wart")
	public boolean wart=true;
	
	@Expose @SerializedName("potato")
	public boolean potato=true;
	
	@Expose @SerializedName("pumpkin")
	public boolean pumpkin=false;
	
	@Expose @SerializedName("wheat")
	public boolean wheat=true;
	
	@Expose @SerializedName("maxCount")
	public int maxCount=0;
	
	public String getFasterBFSString(){
		return "fasterBFS";
	}
	
	public String[] getCropList(){
		return new String[]{
			"beetroot","carrot","melon",
			"wart","potato","pumpkin","wheat"
		};
	}
	
	public String getOptString(String name){
		return Lang.get("farmhelper.setting."+name.toLowerCase());
	}
	
	public void init(){
		LiteLoader.getInstance().registerExposable(this,null);
		verifyMaxCount();
	}
	
	public void save(){
		verifyMaxCount();
		LiteLoader.getInstance().writeConfig(this);
	}
	
	public boolean getBoolean(String key,boolean defaultValue){
		try{
			Field f=Config.class.getField(key);
			return f.getBoolean(this);
		}catch(Exception e){
			return defaultValue;
		}
	}
	public void putBoolean(String key,boolean value){
		try{
			Field f=Config.class.getField(key);
			f.setBoolean(this,value);
		}catch(Exception e){
		}
	}
	
	private void verifyMaxCount(){
		if(maxCount<0) maxCount=0;
		else if(maxCount>64) maxCount=64;
	}
}
