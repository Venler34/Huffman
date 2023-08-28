import java.util.*;
import java.io.*;
public class HuffmanCompressor
{
    public static void compress(String fileName)
    {
        Scanner scan = null;
        try
        {
            scan = new Scanner(new File(fileName));
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        int[] frequencies = new int[256];
        while(scan.hasNextLine())
        {
            String str = scan.nextLine();
            for(int index : str.toCharArray())
            {
                frequencies[index]++;
            }
            if(scan.hasNextLine())
            {
                frequencies[(int)'\n']++;
            }
        }
        HuffmanTree myHuffmanTree = new HuffmanTree(frequencies);
        int length = fileName.length();
        String treeFile = fileName.substring(0,length-4) + ".code";
        myHuffmanTree.write(treeFile);
        String shortFile = fileName.substring(0,length-4) + ".short";
        myHuffmanTree.encode(new BitOutputStream(shortFile),fileName);
    }
    public static void expand(String codeFile, String fileName)
    {
        //code file is human readable and is the tree
        HuffmanTree myHuffmanTree = new HuffmanTree(codeFile);
        //fileName should be .new
        String name = fileName.substring(0,fileName.length()-"short".length()) + "new";
        myHuffmanTree.decode(new BitInputStream(fileName),name);
    }
}