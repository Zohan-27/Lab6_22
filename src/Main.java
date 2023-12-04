import java.io.*;
import java.util.Scanner;

interface InformationProvider {
    String getInfo();
}

abstract class ElectronicDevice implements InformationProvider {
    private String brand;
    protected double price;

    public ElectronicDevice(String brand, double price) {
        this.brand = brand;
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

class Cable extends ElectronicDevice {
    private int length;

    public Cable(String brand, double price, int length) {
        super(brand, price);
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public String getInfo() {
        return "Кабель [бренд=" + getBrand() + ", цена=" + getPrice() + ", длина=" + length + "]";
    }
}

class Capability extends ElectronicDevice {
    private String feature;

    public Capability(String brand, double price, String feature) {
        super(brand, price);
        this.feature = feature;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    @Override
    public String getInfo() {
        return "Возможность [бренд=" + getBrand() + ", цена=" + getPrice() + ", особенность=" + feature + "]";
    }
}

class Case extends ElectronicDevice {
    private String material;

    public Case(String brand, double price, String material) {
        super(brand, price);
        this.material = material;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    @Override
    public String getInfo() {
        return "Корпус [бренд=" + getBrand() + ", цена=" + getPrice() + ", материал=" + material + "]";
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        InformationProvider[] infoProviderDevices = new InformationProvider[10];
        int count = 0;
        boolean continueInput = true;

        while (continueInput && count < infoProviderDevices.length) {
            System.out.println("Введите данные для устройства " + (count + 1) + ":");
            System.out.print("Бренд: ");
            String brand = scanner.nextLine();
            System.out.print("Цена: ");
            double price = scanner.nextDouble();
            scanner.nextLine();

            System.out.println("Выберите тип устройства:");
            System.out.println("1. Кабель");
            System.out.println("2. Возможность");
            System.out.println("3. Корпус");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Введите длину кабеля: ");
                    int length = scanner.nextInt();
                    scanner.nextLine();
                    Cable cable = new Cable(brand, price, length);
                    infoProviderDevices[count] = cable;
                    break;
                case 2:
                    System.out.print("Введите особенность устройства: ");
                    String feature = scanner.nextLine();
                    Capability capability = new Capability(brand, price, feature);
                    infoProviderDevices[count] = capability;
                    break;
                case 3:
                    System.out.print("Введите материал корпуса: ");
                    String material = scanner.nextLine();
                    Case caseDevice = new Case(brand, price, material);
                    infoProviderDevices[count] = caseDevice;
                    break;
                default:
                    System.out.println("Неверный выбор. Пожалуйста, выберите 1, 2 или 3.");
                    continue;
            }

            System.out.println("Выберите тип записи в файл:");
            System.out.println("1. FileOutputStream");
            System.out.println("2. BufferedOutputStream");
            System.out.println("3. ByteArrayOutputStream");
            int writeChoice = scanner.nextInt();
            scanner.nextLine();

            switch (writeChoice) {
                case 1:
                    writeDeviceInfoToFile(infoProviderDevices[count], "deviceInfo.txt");
                    break;
                case 2:
                    writeDeviceInfoToFile(infoProviderDevices[count], "deviceInfo.txt", true);
                    break;
                case 3:
                    String data = writeDeviceInfoToByteArray(infoProviderDevices[count]);
                    writeByteArrayToFile(data, "deviceInfo.txt");
                    break;
                default:
                    writeDeviceInfoToFile(infoProviderDevices[count], "deviceInfo.txt");
                    break;
            }

            count++;

            System.out.print("Продолжить ввод данных? (y/n): ");
            String input = scanner.nextLine();
            if (!input.equalsIgnoreCase("y")) {
                continueInput = false;
            }
        }

        System.out.println("\nИнформация об устройствах:");
        for (int i = 0; i < count; i++) {
            System.out.println(infoProviderDevices[i].getInfo());
        }

        System.out.println("\nЧтение информации из файла:");
        readDeviceInfoFromFile("deviceInfo.txt");

        scanner.close();
    }

    private static void writeDeviceInfoToFile(InformationProvider device, String fileName) {
        try (PrintStream printStream = new PrintStream(new FileOutputStream(fileName, true))) {
            String info = device.getInfo() + "\n";
            printStream.println(info);
            System.out.println("Информация успешно записана в файл " + fileName);
        } catch (IOException e) {
            System.out.println("Ошибка при записи данных в файл: " + e.getMessage());
        }
    }

    private static void writeDeviceInfoToFile(InformationProvider device, String fileName, boolean useBuffered) {
        try (PrintStream printStream = new PrintStream(new BufferedOutputStream(new FileOutputStream(fileName, true)))) {
            String info = device.getInfo() + "\n";
            printStream.println(info);
            System.out.println("Информация успешно записана в файл " + fileName);
        } catch (IOException e) {
            System.out.println("Ошибка при записи данных в файл: " + e.getMessage());
        }
    }

    private static String writeDeviceInfoToByteArray(InformationProvider device) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (PrintStream printStream = new PrintStream(byteArrayOutputStream)) {
            String info = device.getInfo() + "\n";
            printStream.println(info);
            return byteArrayOutputStream.toString();
        } catch (Exception e) {
            System.out.println("Ошибка при записи данных в ByteArrayOutputStream: " + e.getMessage());
        }
        return null;
    }

    private static void writeByteArrayToFile(String data, String fileName) {
        try (PrintStream printStream = new PrintStream(new FileOutputStream(fileName, true))) {
            printStream.println(data);
            System.out.println("Информация успешно записана в файл " + fileName);
        } catch (IOException e) {
            System.out.println("Ошибка при записи данных из ByteArrayOutputStream в файл: " + e.getMessage());
        }
    }

    private static void readDeviceInfoFromFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Ошибка при чтении данных из файла: " + e.getMessage());
        }
    }
}
