import java.util.*;

interface FileSystemItem {
	void ls(int indent);
	void openAll(int indent);
	int getSize();
	FileSystemItem cd(String name);
	String getName();
	boolean isFolder();
	void add(FileSystemItem item);
}

class File implements FileSystemItem {
	private String name;
	private int size;
	public File(String name, int size)
	{
		this.name = name;
		this.size = size;
	}
	public void add(FileSystemItem item){
	    return;
	}
	public void ls(int indent)
	{
		String indentation = " ".repeat(indent);
		System.out.println(indentation+name);
	}

	public void openAll(int indent)
	{
		String indentation = " ".repeat(indent);
		System.out.println(indentation+name);
	}

	public int getSize()
	{
		return size;
	}

	public String getName()
	{
		return name;
	}

	public boolean isFolder()
	{
		return false;
	}

	public FileSystemItem cd(String name)
	{
		return null;
	}
}

class Folder implements FileSystemItem {
	private String name;
	List<FileSystemItem> children;
	public Folder(String name)
	{
		this.name = name;
		children = new ArrayList<>();
	}
	public void add(FileSystemItem item)
	{
		if(!children.contains(item))
		{
			children.add(item);
		}
	}
	public void ls(int indent)
	{
		String indentation = " ".repeat(indent);
		System.out.println(name);
		for(FileSystemItem item : children)
		{
			System.out.println(indentation + item.getName());
		}
	}

	public void openAll(int indent)
	{
		String indentation = " ".repeat(indent);
		System.out.println(indentation + name);
		for(FileSystemItem item : children)
		{
			item.openAll(indent+5);
		}
	}

	public int getSize()
	{
		int size = 0;
		for(FileSystemItem item : children)
		{
			size += item.getSize();
		}
		return size;
	}

	public String getName()
	{
		return name;
	}

	public boolean isFolder()
	{
		return true;
	}

	public FileSystemItem cd(String name)
	{
		for(FileSystemItem item : children)
		{
			if(item.getName()==name && item.isFolder())
			{
				return item;
			}
		}
		return null;
	}
}

public class Main {
	public static void main(String[] args)
	{
		FileSystemItem root = new Folder("RootFolder");
		FileSystemItem folder1 = new Folder("folder1");
		FileSystemItem folder2 = new Folder("folder2");

		FileSystemItem file1 = new File("file1",4);
		FileSystemItem file2 = new File("file2",4);
		FileSystemItem file3 = new File("file3",4);
		FileSystemItem file4 = new File("file4",4);
		FileSystemItem file5 = new File("file5",4);
		FileSystemItem file6 = new File("file6",4);
		FileSystemItem file7 = new File("file7",4);
		FileSystemItem file8 = new File("file8",4);
		FileSystemItem file9 = new File("file9",4);
		FileSystemItem file10 = new File("file10",4);

		root.add(file1);
		root.add(file2);
		folder1.add(file3);
		folder1.add(file4);
		folder1.add(file5);
		folder1.add(file6);
		folder1.add(file7);

		folder2.add(file8);
		folder2.add(file9);
		folder2.add(file10);

		root.add(folder1);
		root.add(folder2);

		FileSystemItem currentItem = root;

		System.out.println(currentItem.getSize() + " KB");
		System.out.println(currentItem.getName());
		System.out.println(currentItem.isFolder());
		currentItem.ls(5);
		currentItem.openAll(0);
		
		System.out.println("=======================================================================");
		
		currentItem = currentItem.cd(folder1.getName());
		System.out.println(currentItem.getSize() + " KB");
		System.out.println(currentItem.getName());
		System.out.println(currentItem.isFolder());
		currentItem.ls(5);
		currentItem.openAll(0);
		
		System.out.println("=======================================================================");
		currentItem = root.cd(folder2.getName());
		System.out.println(currentItem.getSize() + " KB");
		System.out.println(currentItem.getName());
		System.out.println(currentItem.isFolder());
		currentItem.ls(5);
		currentItem.openAll(0);
		
		System.out.println("=======================================================================");
		currentItem = file7;
		System.out.println(currentItem.getSize() + " KB");
		System.out.println(currentItem.getName());
		System.out.println(currentItem.isFolder());
		currentItem.ls(0);
		currentItem.openAll(0);
	}
}
