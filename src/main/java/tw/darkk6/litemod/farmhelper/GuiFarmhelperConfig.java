package tw.darkk6.litemod.farmhelper;

import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiTextField;
import tw.darkk6.litemod.farmhelper.plant.PlantManager;
import tw.darkk6.litemod.farmhelper.util.Config;
import tw.darkk6.litemod.farmhelper.util.Lang;

import com.mumfrey.liteloader.client.gui.GuiCheckbox;
import com.mumfrey.liteloader.modconfig.ConfigPanel;
import com.mumfrey.liteloader.modconfig.ConfigPanelHost;

public class GuiFarmhelperConfig extends Gui implements ConfigPanel {

	private Minecraft mc=Minecraft.getMinecraft();
	private HashMap<String,GuiCheckbox> boxList;
	private GuiLabel lb,lbMD;
	private GuiTextField txt,txtMD;
	private Rect infoPos;
	
	private int theLabelWidth,labelMDWidth;
	
	@Override
	public String getPanelTitle() { return Lang.get("farmhelper.setting.title"); }

	@Override
	public int getContentHeight() {return 0;}

	@Override
	public void onPanelShown(ConfigPanelHost host) {
		int controlID=0;
		int wCenter=host.getWidth()/2;
		int fontHeight=mc.fontRendererObj.FONT_HEIGHT;
		int lineCount=1;
		if(boxList==null){
			int idx=0;
			boxList=new HashMap<String, GuiCheckbox>();
			//加入 fasterBFS
			GuiCheckbox ch=new GuiCheckbox(controlID, 10 ,10,Config.get().getOptString(Config.get().getFasterBFSString()));
			boxList.put(Config.get().getFasterBFSString(),ch);
			ch.checked=Config.get().getBoolean(Config.get().getFasterBFSString(),false);
			controlID++;
			//加入 Crops List
			for(String key:Config.get().getCropList()){
				ch=new GuiCheckbox(controlID+idx, 
						10 + wCenter*(idx%2), 
						10 +(fontHeight+5)*((idx/2)+1), 
						Config.get().getOptString(key)
					);
				boxList.put(key,ch);
				ch.checked=Config.get().getBoolean(key,true);
				if(idx%2==0) lineCount++;//紀錄總共有幾行了
				idx++;
			}
			//加入 RequestUpdate
			controlID+=idx;
			int yPos=lineCount*(fontHeight+5)+20;
			ch=new GuiCheckbox(controlID,10, yPos,Config.get().getOptString(Config.get().getRequstUpdateStr()));
			boxList.put(Config.get().getRequstUpdateStr(),ch);
			ch.checked=Config.get().getBoolean(Config.get().getRequstUpdateStr(),false);
			//加入最大距離
			String tmp=Lang.get("farmhelper.setting.maxdistance");
			int strWidth=mc.fontRendererObj.getStringWidth(tmp);
			lbMD=new GuiLabel(mc.fontRendererObj, 100, wCenter+10,yPos,strWidth,fontHeight+4,0xFFFFFFFF);
			lbMD.addLine(tmp);
			labelMDWidth=strWidth;
			
			txtMD=new GuiTextField(101, mc.fontRendererObj,wCenter+strWidth+15,yPos,50,fontHeight+4);
			txtMD.setText(String.valueOf(Config.get().maxDistance));
			lineCount++;
		}
		
		if(txt==null || lb==null){
			String tmp=Lang.get("farmhelper.setting.maxcount");
			int strWidth=mc.fontRendererObj.getStringWidth(tmp);
			lb=new GuiLabel(mc.fontRendererObj, 102, wCenter+10,10,strWidth,fontHeight+4,0xFFFFFFFF);
			lb.addLine(tmp);
			theLabelWidth=strWidth;
			
			txt=new GuiTextField(103, mc.fontRendererObj,wCenter+strWidth+15,10,50,fontHeight+4);
			txt.setText(String.valueOf(Config.get().maxCount));
			
			strWidth=mc.fontRendererObj.getStringWidth(tmp);
			int yPos=(lineCount+1)*(fontHeight+5)+20;
			infoPos=new Rect(10,yPos,strWidth,fontHeight+4);
		}
	}
	@Override
	public void onPanelHidden() {
		//儲存 fasterBFS
		Config.get().fasterBFS=boxList.get(Config.get().getFasterBFSString()).checked;
		//儲存 RequestUpdate
		Config.get().requestUpdate=boxList.get(Config.get().getRequstUpdateStr()).checked;
		//儲存 Crops
		for(String key:boxList.keySet()){
			Config.get().putBoolean(key,boxList.get(key).checked);
		}
		//儲存 MaxCount
		if(txt.getText().length()<=0) Config.get().maxCount=0;
		else Config.get().maxCount=Integer.parseInt(txt.getText());
		//儲存 MaxDistance
		if(txtMD.getText().length()<=0) Config.get().maxDistance=0;
		else Config.get().maxDistance=Integer.parseInt(txtMD.getText());
		
		Config.get().save();
		//更新資料
		PlantManager.update();
	}
	@Override
	public void drawPanel(ConfigPanelHost host, int mouseX, int mouseY, float partialTicks) {
		for(String key:boxList.keySet()){
			boxList.get(key).drawButton(mc, mouseX, mouseY);
		}
		lbMD.drawLabel(mc, mouseX, mouseY);
		txtMD.drawTextBox();
		lb.drawLabel(mc, mouseX, mouseY);
		txt.drawTextBox();
		drawInfoLine(Lang.get("farmhelper.setting.warning"),1,0xFFFF7373);
		checkMouseHover(mouseX, mouseY);
	}
	@Override
	public void keyPressed(ConfigPanelHost host, char keyChar, int keyCode) {
		//限制只能輸入數字
		if( keyChar>57 || (keyChar<48 && keyChar!=8)) return;
		if(txt.isFocused()){
			txt.textboxKeyTyped(keyChar, keyCode);
		}else if(txtMD.isFocused()){
			txtMD.textboxKeyTyped(keyChar, keyCode);
		}
	}
	@Override
	public void mousePressed(ConfigPanelHost host, int mouseX, int mouseY, int mouseButton) {
		txt.mouseClicked(mouseX, mouseY, mouseButton);
		if(txt.isFocused()) return;
		txtMD.mouseClicked(mouseX, mouseY, mouseButton);
		if(txtMD.isFocused()) return;
		
		for(String key:boxList.keySet()){
			GuiCheckbox ch=boxList.get(key);
			if(ch.mousePressed(mc, mouseX, mouseY)){
				ch.checked=!ch.checked;
				return;
			}
		}
	}
	
	private void checkMouseHover(int mouseX,int mouseY){
		int fontHeight=mc.fontRendererObj.FONT_HEIGHT;
		boolean isTextHover = mouseX >= txt.xPosition && mouseX < txt.xPosition + txt.getWidth() && mouseY >= txt.yPosition && mouseY < txt.yPosition + fontHeight;
		boolean isLabelHover = mouseX >= lb.x && mouseX < lb.x + theLabelWidth && mouseY >= txt.yPosition && mouseY < txt.yPosition + fontHeight;
		boolean isTextMDHover = mouseX >= txtMD.xPosition && mouseX < txtMD.xPosition + txtMD.getWidth() && mouseY >= txtMD.yPosition && mouseY < txtMD.yPosition + fontHeight;
		boolean isLabelMDHover = mouseX >= lbMD.x && mouseX < lbMD.x + labelMDWidth && mouseY >= txtMD.yPosition && mouseY < txtMD.yPosition + fontHeight;
		if(isTextHover||isLabelHover){
			drawInfoLine(Lang.get("farmhelper.setting.maxcount.comment"),2,0xFFFFFFFF);
		}else if(isTextMDHover||isLabelMDHover){
			drawInfoLine(Lang.get("farmhelper.setting.maxdistance.comment"),2,0xFFFFFFFF);
		}else if(boxList.get(Config.get().getFasterBFSString()).isMouseOver()){
			drawInfoLine(Lang.get("farmhelper.setting.fasterbfs.comment"),2,0xFFFFFFFF);
		}else if(boxList.get(Config.get().getRequstUpdateStr()).isMouseOver()){
			drawInfoLine(Lang.get("farmhelper.setting.requestupdate.comment"),2,0xFFFFFFFF);
		}
	}
	
	private void drawInfoLine(String text,int line,int color){
		if(line<=0) return;
		line--;
		int fontHeight=mc.fontRendererObj.FONT_HEIGHT;
		drawString(mc.fontRendererObj,text,infoPos.x,infoPos.y+(fontHeight+4)*line,color);
	}

	@Override
	public void mouseMoved(ConfigPanelHost host, int mouseX, int mouseY) {}
	@Override
	public void mouseReleased(ConfigPanelHost host, int mouseX, int mouseY, int mouseButton) {}
	@Override
	public void onPanelResize(ConfigPanelHost host) {}
	@Override
	public void onTick(ConfigPanelHost host) {}
	
	private class Rect{
		public int x,y,width,height;
		public Rect(int x,int y,int w,int h){
			this.x=x;
			this.y=y;
			this.width=w;
			this.height=h;
		}
	}
}
