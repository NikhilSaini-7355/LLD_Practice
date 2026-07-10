class PowerSupply {
	public void providePower()
	{
		System.out.println("Providing Power");
	}
}

class CoolingSystem {
	public void coolSystem()
	{
		System.out.println("cooling the system");
	}
}

class CPU {
	public void initialize()
	{
		System.out.println("initializing the cpu");
	}
}

class Memory {
	public void selfTest()
	{
		System.out.println("Setting up the memory");
	}
}

class HardDrive {
	public void spinUp()
	{
		System.out.println("Spinning up the Hard Drive");
	}
}

class BIOS {
	public void boot(CPU cpu, Memory memory)
	{
		System.out.println("Booting the BIOS");
		cpu.initialize();
		memory.selfTest();
	}
}
class OperatingSystem {
	public void load()
	{
		System.out.println("loading the operating system");
	}
}

class ComputerFacade {
	PowerSupply power = new PowerSupply();
	CoolingSystem cooler = new CoolingSystem();
	HardDrive drive = new HardDrive();
	BIOS bios = new BIOS(); 
	OperatingSystem os = new OperatingSystem();
	CPU cpu = new CPU();
	Memory memory = new Memory();
	public void startComputer() {
	    this.power.providePower();
	    this.cooler.coolSystem();
	    this.drive.spinUp();
	    this.bios.boot(this.cpu, this. memory);
	    this.os.load();
	}
}

public class Main{
    public static void main(String[] args)
    {
        ComputerFacade facade = new ComputerFacade();
        facade.startComputer();
    }
}
