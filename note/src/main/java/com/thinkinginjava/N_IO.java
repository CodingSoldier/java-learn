package com.thinkinginjava;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.zip.*;

import static com.thinkinginjava.E16_UsingAllRandomAccessFile.print;

public class N_IO {
    public static String dic = "./note/src/main/java/com/thinkinginjava/";
}


//  P528
class DirList3 {
    public static void main(final String[] args) {
        File filePath = new File(".");
        System.out.println(filePath.list());

        String[] list = filePath.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return Pattern.compile(".java").matcher(name).matches();
            }
        });
        System.out.println(list);
    }
}


//工具类
class TextFile extends ArrayList<String> {
    // Read a file, split by any regular expression:
    public TextFile(String fileName, String splitter) {
        super(Arrays.asList(read(fileName).split(splitter)));
        // Regular expression split() often leaves an empty
        // String at the first position:
        if (get(0).equals("")) remove(0);
    }

    // Normally read by lines:
    public TextFile(String fileName) {
        this(fileName, "\n");
    }

    // Read a file as a single string:
    public static String read(String fileName) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new FileReader(
                    new File(fileName).getAbsoluteFile()));
            try {
                String s;
                while ((s = in.readLine()) != null) {
                    sb.append(s);
                    sb.append("\n");
                }
            } finally {
                in.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    // Write a single file in one method call:
    public static void write(String fileName, String text) {
        try {
            PrintWriter out = new PrintWriter(
                    new File(fileName).getAbsoluteFile());
            try {
                out.print(text);
            } finally {
                out.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Simple test:
    public static void main(String[] args) {
        //String file = read("TextFile.java");
        //write("test.txt", file);
        //TextFile text = new TextFile("test.txt");
        //text.write("test2.txt");
        //// Break into unique sorted list of words:
        //TreeSet<String> words = new TreeSet<String>(
        //        new TextFile("TextFile.java", "\\W+"));
        //// Display the capitalized words:
        //System.out.println(words.headSet("a"));
    }

    public void write(String fileName) {
        try {
            PrintWriter out = new PrintWriter(
                    new File(fileName).getAbsoluteFile());
            try {
                for (String item : this)
                    out.println(item);
            } finally {
                out.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


final class Directory {
    public static File[]
    local(File dir, final String regex) {
        return dir.listFiles(new FilenameFilter() {
            private Pattern pattern = Pattern.compile(regex);

            public boolean accept(File dir, String name) {
                return pattern.matcher(
                        new File(name).getName()).matches();
            }
        });
    }

    public static File[]
    local(String path, final String regex) { // Overloaded
        return local(new File(path), regex);
    }

    public static TreeInfo
    walk(String start, String regex) { // Begin recursion
        return recurseDirs(new File(start), regex);
    }

    public static TreeInfo
    walk(File start, String regex) { // Overloaded
        return recurseDirs(start, regex);
    }

    public static TreeInfo walk(File start) { // Everything
        return recurseDirs(start, ".*");
    }

    public static TreeInfo walk(String start) {
        return recurseDirs(new File(start), ".*");
    }

    static TreeInfo recurseDirs(File startDir, String regex) {
        TreeInfo result = new TreeInfo();
        for (File item : startDir.listFiles()) {
            if (item.isDirectory()) {
                result.dirs.add(item);
                result.addAll(recurseDirs(item, regex));
            } else // Regular file
                if (item.getName().matches(regex))
                    result.files.add(item);
        }
        return result;
    }

    // Simple validation test:
    public static void main(String[] args) {
        if (args.length == 0)
            System.out.println(walk("."));
        else
            for (String arg : args)
                System.out.println(walk(arg));
    }

    // A two-tuple for returning a pair of objects:
    public static class TreeInfo implements Iterable<File> {
        public List<File> files = new ArrayList<File>();
        public List<File> dirs = new ArrayList<File>();

        // The default iterable element is the file list:
        public Iterator<File> iterator() {
            return files.iterator();
        }

        void addAll(TreeInfo other) {
            files.addAll(other.files);
            dirs.addAll(other.dirs);
        }

        public String toString() {
            return "dirs: " + dirs +
                    "\n\nfiles: " + files;
        }
    }
}

class ProcessFiles {
    private Strategy strategy;
    private String ext;
    public ProcessFiles(Strategy strategy, String ext) {
        this.strategy = strategy;
        this.ext = ext;
    }

    // Demonstration of how to use it:
    public static void main(String[] args) {
        new ProcessFiles(new ProcessFiles.Strategy() {
            public void process(File file) {
                System.out.println(file);
            }
        }, "java").start(new String[]{N_IO.dic});
    }

    public void start(String[] args) {
        try {
            if (args.length == 0)
                processDirectoryTree(new File("."));
            else
                for (String arg : args) {
                    File fileArg = new File(arg);
                    if (fileArg.isDirectory())
                        processDirectoryTree(fileArg);
                    else {
                        // Allow user to leave off extension:
                        if (!arg.endsWith("." + ext))
                            arg += "." + ext;
                        strategy.process(
                                new File(arg).getCanonicalFile());
                    }
                }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void processDirectoryTree(File root) throws IOException {
        for (File file : Directory.walk(
                root.getAbsolutePath(), ".*\\." + ext))
            strategy.process(file.getCanonicalFile());
    }

    public interface Strategy {
        void process(File file);
    }
}


// P544
class E16_UsingAllRandomAccessFile {
    static String file = "rtest.dat";

    static void display() throws IOException {
        RandomAccessFile rf = new RandomAccessFile(file, "r");
        print(rf.readBoolean());
        print(rf.readByte());
        print(rf.readUnsignedByte());
        print(rf.readChar());
        print(rf.readFloat());
        print(rf.readLong());
        print(rf.readInt());
        print(rf.readShort());
        print(rf.readUnsignedShort());
        print(rf.readDouble());
        print(rf.readUTF());
        rf.close();
    }

    static public void print(Object o) {
        System.out.println(o);
    }

    public static void main(String[] args)
            throws IOException {
        RandomAccessFile rf = new RandomAccessFile(file, "rw");
        rf.writeBoolean(true);
        rf.writeByte(100);
        rf.writeByte(255);
        rf.writeChar('A');
        rf.writeFloat(1.41413f);
        rf.writeLong(1000000000L);
        rf.writeInt(100000);
        rf.writeShort(30000);
        rf.writeShort(65535);
        rf.writeDouble(3.14159);
        rf.writeUTF("The end of the file");
        rf.close();
        display();
        rf = new RandomAccessFile(file, "rw");
        rf.seek(3); // 1 boolean + 2 bytes
        rf.writeChar('B');
        rf.close();
        display();
    }
}


//P569  查找文件路径可通过 new File(".")一点点的往下找
class ZipCompress {
    public static void main(String[] args) throws IOException {
        args = new String[]{"./note/src/main/java/com/thinkinginjava/N_IO.java"};
        FileOutputStream f = new FileOutputStream("test.zip");
        CheckedOutputStream csum = new CheckedOutputStream(f, new Adler32());
        ZipOutputStream zos = new ZipOutputStream(csum);
        BufferedOutputStream out = new BufferedOutputStream(zos);
        zos.setComment("A test of Java Zipping");
        // No corresponding getComment(), though.
        for (String arg : args) {
            print("Writing file " + arg);

            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(arg)));
            zos.putNextEntry(new ZipEntry(arg));
            String c;
            while ((c = in.readLine()) != null)
                //输入时读取行，变成string；BufferedOutputStream输出时对字符串编码
                out.write((c+"\n").getBytes("utf-8"));
            in.close();
            out.flush();
        }
        out.close();
        // Checksum valid only after the file has been closed!
        print("Checksum: " + csum.getChecksum().getValue());
        // Now extract the files:
        print("Reading file");
        FileInputStream fi = new FileInputStream("test.zip");
        CheckedInputStream csumi = new CheckedInputStream(fi, new Adler32());
        ZipInputStream in2 = new ZipInputStream(csumi);
        BufferedInputStream bis = new BufferedInputStream(in2);
        ZipEntry ze;
        while ((ze = in2.getNextEntry()) != null) {
            print("Reading file " + ze);
            int x;
            while ((x = bis.read()) != -1)
                System.out.write(x);
        }
        if (args.length == 1)
            print("Checksum: " + csumi.getChecksum().getValue());
        bis.close();
        // Alternative way to open and read Zip files:
        ZipFile zf = new ZipFile("test.zip");
        Enumeration e = zf.entries();
        while (e.hasMoreElements()) {
            ZipEntry ze2 = (ZipEntry) e.nextElement();
            print("File: " + ze2);
            // ... and extract the data as before
        }
    }
}