package tw.darkk6.litemod.farmhelper.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {
private static Logger log=LogManager.getLogger(Reference.LOG_TAG);
	
	public static Logger info(String msg){
		log.info(msg);
		return log;
	}
	
	public static void chatGuiLog(TextComponentString txt){
		Minecraft.getMinecraft().thePlayer.addChatComponentMessage(txt);
	}
	
	//顯示主手必須要是空的訊息
	public static void showMainHandMustNull(){
		TextComponentString txt = new TextComponentString(TextFormatting.GOLD + "[FarmHelper] ");
		txt.appendText(TextFormatting.RESET+Lang.get("farmhelper.mainhand.mustnull.msg"));
		chatGuiLog(txt);
	}
}
