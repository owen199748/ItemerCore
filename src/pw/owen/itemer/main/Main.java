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
			sender.sendMessage("������ֻ�������ִ��");
			return true;
		} else {
			if (cmd.getName().equalsIgnoreCase("itemer")) {
				Player p = (Player) sender;
				try {
				if (args.length == 0)
 {
					Send.sendPluginMessage(p, "��7��lVersion:"
							+ Main.getMain().getDescription().getVersion());
					Send.sendPluginMessage(p, "��7��l������������ /Itemer help");
					return true;
				}

				// ���ǲ���������д������
				if (args[0].equalsIgnoreCase("help")) {
					ItemStatic.help(p);
					return true;
				}
				if (p.getItemInHand() == null
						|| p.getItemInHand().getType() == Material.AIR) {

				}
				// ����������д������
				ItemStack ph = p.getItemInHand();
				ItemStatic s = ItemStatic.getItemStatic(ph);
				if (ph == null) {
					Send.sendPluginMessage(p, "�����ֳ�һ��װ��");
					return true;
				}
				if (s == null)
					s = ItemStatic.getNewItemStatic();
				if (args[0].equalsIgnoreCase("set")) {
					if (args.length != 4)
						return false;


				if (!ItemStatic.setStatic(args[1], args[2], args[3])) 
					if (!s.set(args[1], args[2], args[3])) {
						Send.sendPluginMessage(p, "����ʧ��,�����ǲ�������.");
						return true;
					}
				} else if (args[0].equalsIgnoreCase("get")) {
					if (args.length < 1)
						return false;

					if (args.length == 1) {
						List<Class<Attributive>> lss = ItemStatic
								.getAttributeClassList();
						String str = "Itemer�����б�:";
						for (int i = 0; i < lss.size(); i++)
							try {
								str += "\n"
										+ lss.get(i).newInstance().getName();
							} catch (Exception e) {
								// e.printStackTrace();
							}
						Send.sendPluginMessage(p, str);
					} else if (args.length == 2) {
						// �о�һ���������͵������Ӳ���
						Attributive attr = ItemStatic.getAttribute(args[1]);
						if (attr == null) {
							Send.sendPluginMessage(p, "����Ĳ������Ͳ�����.");
							return true;
						}
							Map<String, String> lst = attr.getParameters();
						String str = attr.getName() + "���Ӳ����б�:";
							if (lst != null)
						for (int i = 0; i < lst.keySet().size(); i++)
							str += "\n"
									+ lst.keySet().toArray()[i].toString()
									+ " ��������:"
									+ lst.get(lst.keySet().toArray()[i])
											.getName();
						Send.sendPluginMessage(p, str);

						lst = attr.getStaticParameters();
						str = attr.getName() + "�������������б�:";
							if (lst != null)
						for (int i = 0; i < lst.keySet().size(); i++)
							str += "\n"
									+ lst.keySet().toArray()[i].toString()
									+ " ��������:"
									+ lst.get(lst.keySet().toArray()[i])
											.getName();
						Send.sendPluginMessage(p, str);

					} else if (args.length == 3) {
						Attributive attr = ItemStatic.getAttribute(args[1]);
						if (attr == null) {
							Send.sendPluginMessage(p, "����Ĳ������Ͳ�����.");
							return true;
						}
						Object obj = attr.get(args[2]);
						if (obj == null)
							obj = attr.getStatic(args[2]);
						else {
							Send.sendPluginMessage(p,
									"Itemer����" + attr.getName() + "���Ӳ���"
											+ args[2] + "��ֵΪ" + obj);
							return true;
						}
						if (obj == null) {
							Send.sendPluginMessage(p, "������Ӳ���/�������Բ�����.");
							return true;
						} else {
							Send.sendPluginMessage(p,
									"Itemer����" + attr.getName() + "������������"
											+ args[2] + "��ֵΪ" + obj);
						}

					}
				} else
					return false;

				p.setItemInHand(s.putItemStatic(ph));
				Send.sendPluginMessage(p, "�����ɹ�");
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

					Send.sendPluginMessage(p, "��d����쳣:��c��l"
							+ e.getClass().getName());

					if (e instanceof IllegalArgumentException)
						Send.sendPluginMessage(p,
								"��d�쳣ԭ��:��c��l�����쳣(�����޷���ת��Ϊ������)");
					else if (e instanceof NullPointerException)
						Send.sendPluginMessage(p,
								"��d�쳣ԭ��:��c��l��ָ���쳣(Ŀ�����������,�п�������Ϊ�ֶ���������������ð汾����)");
					else
						Send.sendPluginMessage(p, "��d�쳣ԭ��:��c��lδ֪");

					Send.sendPluginMessage(p, "��d����鿴����̨�Ĵ�����Ϣ.");
					e.printStackTrace();
					return true;

				}
			}

		}

		return false;
	}
}

