package pw.owen.itemer.main;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import pw.owen.itemer.bean.ItemStatic;
import pw.owen.itemer.bean.attribute.Attributive;
import pw.owen.itemer.utils.Send;

public class Main extends JavaPlugin{
	private static Main main;
	private static ClassLoader classloader;
	private static File myfile;
	public static final java.text.DecimalFormat FORMAT = new java.text.DecimalFormat(
			"#.##");

	public static Main getMain() {
		return main;
	}

	public static File getMyfile() {
		return myfile;
	}

	public static ClassLoader getClassloader() {
		return classloader;
	}

@Override
public void onEnable() {
		main = this;
		classloader = this.getClassLoader();
		myfile = this.getFile();
		getServer().getPluginManager().registerEvents(new AutoListener(), this);
}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage("本命令只能由玩家执行");
			return true;
		} else {
			if (cmd.getName().equalsIgnoreCase("itemer")) {
				Player p = (Player) sender;
				try {
				if (args.length == 0)
 {
					Send.sendPluginMessage(p, "§7§lVersion:"
							+ Main.getMain().getDescription().getVersion());
					Send.sendPluginMessage(p, "§7§l更多帮助请参照 /Itemer help");
					return true;
				}

				// 不是操作类命令写在这里
				if (args[0].equalsIgnoreCase("help")) {
					ItemStatic.help(p);
					return true;
				}
				if (p.getItemInHand() == null
						|| p.getItemInHand().getType() == Material.AIR) {

				}
				// 操作类命令写在这里
				ItemStack ph = p.getItemInHand();
				ItemStatic s = ItemStatic.getItemStatic(ph);
				if (ph == null) {
					Send.sendPluginMessage(p, "请先手持一件装备");
					return true;
				}
				if (s == null)
					s = ItemStatic.getNewItemStatic();
				if (args[0].equalsIgnoreCase("set")) {
					if (args.length != 4)
						return false;


				if (!ItemStatic.setStatic(args[1], args[2], args[3])) 
					if (!s.set(args[1], args[2], args[3])) {
						Send.sendPluginMessage(p, "设置失败,可能是参数错误.");
						return true;
					}
				} else if (args[0].equalsIgnoreCase("get")) {
					if (args.length < 1)
						return false;

					if (args.length == 1) {
						List<Class<Attributive>> lss = ItemStatic
								.getAttributeClassList();
						String str = "Itemer属性列表:";
						for (int i = 0; i < lss.size(); i++)
							try {
								str += "\n"
										+ lss.get(i).newInstance().getName();
							} catch (Exception e) {
								// e.printStackTrace();
							}
						Send.sendPluginMessage(p, str);
					} else if (args.length == 2) {
						// 列举一个参数类型的所有子参数
						Attributive attr = ItemStatic.getAttribute(args[1]);
						if (attr == null) {
							Send.sendPluginMessage(p, "输入的参数类型不存在.");
							return true;
						}
							Map<String, String> lst = attr.getParameters();
						String str = attr.getName() + "的子参数列表:";
							if (lst != null)
						for (int i = 0; i < lst.keySet().size(); i++)
							str += "\n"
									+ lst.keySet().toArray()[i].toString()
									+ " 类型限制:"
									+ lst.get(lst.keySet().toArray()[i])
											.getName();
						Send.sendPluginMessage(p, str);

						lst = attr.getStaticParameters();
						str = attr.getName() + "的子设置属性列表:";
							if (lst != null)
						for (int i = 0; i < lst.keySet().size(); i++)
							str += "\n"
									+ lst.keySet().toArray()[i].toString()
									+ " 类型限制:"
									+ lst.get(lst.keySet().toArray()[i])
											.getName();
						Send.sendPluginMessage(p, str);

					} else if (args.length == 3) {
						Attributive attr = ItemStatic.getAttribute(args[1]);
						if (attr == null) {
							Send.sendPluginMessage(p, "输入的参数类型不存在.");
							return true;
						}
						Object obj = attr.get(args[2]);
						if (obj == null)
							obj = attr.getStatic(args[2]);
						else {
							Send.sendPluginMessage(p,
									"Itemer类型" + attr.getName() + "的子参数"
											+ args[2] + "的值为" + obj);
							return true;
						}
						if (obj == null) {
							Send.sendPluginMessage(p, "输入的子参数/设置属性不存在.");
							return true;
						} else {
							Send.sendPluginMessage(p,
									"Itemer类型" + attr.getName() + "的子设置属性"
											+ args[2] + "的值为" + obj);
						}

					}
				} else
					return false;

				p.setItemInHand(s.putItemStatic(ph));
				Send.sendPluginMessage(p, "操作成功");
					return true;
				} catch (Exception e) {

					StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw);
					e.printStackTrace(pw);
					String str = "";

					for (int i = 0; i < args.length; i++) {
						if (i != 0)
							str += " ";

						str += args[i];
					}

					Send.sendPluginMessage(p, "§d插件异常:§c§l"
							+ e.getClass().getName());

					if (e instanceof IllegalArgumentException)
						Send.sendPluginMessage(p,
								"§d异常原因:§c§l参数异常(参数无法被转换为整数形)");
					else if (e instanceof NullPointerException)
						Send.sendPluginMessage(p,
								"§d异常原因:§c§l空指针异常(目标参数不存在,有可能是因为手动配置有误或者配置版本有误)");
					else
						Send.sendPluginMessage(p, "§d异常原因:§c§l未知");

					Send.sendPluginMessage(p, "§d详情查看控制台的错误信息.");
					e.printStackTrace();
					return true;

				}
			}

		}

		return false;
	}
}

