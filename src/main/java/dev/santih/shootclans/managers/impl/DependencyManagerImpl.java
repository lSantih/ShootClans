package dev.santih.shootclans.managers.impl;

import dev.santih.shootclans.managers.interfaces.DependencyManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class DependencyManagerImpl implements DependencyManager {
	private JavaPlugin plugin;
	private File libFolder;

	public DependencyManagerImpl(JavaPlugin plugin, String libFolderPath) {
		this.plugin = plugin;
		this.libFolder = new File(libFolderPath);
	}

	@Override
	public void loadLibrary(String libraryFileName) {
		long start = System.currentTimeMillis();
		try {
			File libraryFile = new File(libFolder, libraryFileName);
			URLClassLoader classLoader = (URLClassLoader) plugin.getClass().getClassLoader();
			Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
			method.setAccessible(true);
			method.invoke(classLoader, new Object[]{libraryFile.toURI().toURL()});
			Bukkit.getLogger().info("[Dependency-Manager] Loaded dependency " + libraryFileName + ". Took " + (System.currentTimeMillis() - start) + " ms.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
