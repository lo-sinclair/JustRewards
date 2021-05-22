package locb.jp.justrewards.tools;


import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.NBTTagList;
import net.minecraft.server.v1_16_R3.NBTTagString;

public class Craft {
	
	public static ItemStack craftBit() {
		ItemStack item = new ItemStack(Material.IRON_NUGGET);
//		ItemMeta meta = item.getItemMeta();
//		meta.setDisplayName(ChatColor.RESET + "" + ChatColor.GRAY + "Ценный металл");
//		List<String> lore = new ArrayList<String>();
//		lore.add("блестящий кусочек металла");
//		lore.add("оцененный банком в 1 bit");
//		meta.setLore(lore);
//		item.setItemMeta(meta);
//		
		NBTTagCompound tag = new NBTTagCompound();
		NBTTagCompound display = new NBTTagCompound();
		//NBTTagCompound name = new NBTTagCompound();
		NBTTagList lore = new NBTTagList();
		lore.add(NBTTagString.a("{\"text\":\"блестящий кусочек металла,\"}"));
		lore.add(NBTTagString.a("{\"text\":\"оценённый банком в 1 bit\"}"));
		display.setString("Name", "{\"text\":\"Ценный металл\",\"italic\":false,\"color\":\"gray\"}");
		display.set("Lore", lore);
		tag.set("display", display);
		
		net.minecraft.server.v1_16_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		nmsItem.setTag(tag);
		
		return CraftItemStack.asBukkitCopy(nmsItem);
	}

}
