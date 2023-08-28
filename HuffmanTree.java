import java.util.*;
import java.io.*;
public class HuffmanTree
{
    private Node root;
    private HashMap<Integer, String> map;
    HuffmanTree(int[] count)//constructs huffman tree
    {
        //count is an array of frequencies
        PriorityQueue<Node> q = new PriorityQueue<>();
        for(int i = 0; i < count.length; i++)
        {
            if(count[i] > 0)
            {
                //new Node(frequency,integer value)
                q.offer(new Node(count[i],i));
            }
        }
        
        //EOF character
        q.offer(new Node(1,256));
        
        
        while(q.size() > 1)//its going to be one tree
        {
            Node c1 = q.poll();
            Node c2 = q.poll();
            Node combine = new Node(c1.weight + c2.weight, null);
            combine.left = c1;
            combine.right = c2;
            q.offer(combine);
        }
        
        root = q.poll();
    }
    HuffmanTree(String codeFile)
    {
        Scanner scan = null;
        try
        {
            scan = new Scanner(new File(codeFile));
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        
        root = new Node(0, null);
        
        while(scan.hasNextLine())
        {
            int n = Integer.parseInt(scan.nextLine());
            String code = scan.nextLine();
            //adds node
            Node current = root;
            for(int i = 0; i < code.length(); i++)
            {
                boolean goRight = (code.charAt(i) == '1');
                if(goRight)
                {
                    if(current.right == null)
                    {
                        //new Node(weight,integer)
                        current.right = new Node(-1,null);
                    }
                    current = current.right;
                }
                else
                {
                    if(current.left == null)
                    {
                        //new Node(weight,integer)
                        current.left = new Node(-1,null);
                    }
                    current = current.left;
                }
            }
            current.myChar = n;
        }
    }
    public void write(String fileName)
    {
        //write encoding tree to the given file in a standard format
        //use printWriter -> yes
        PrintWriter diskFile = null;
        
        //HashMap for encoding file
        map = new HashMap<>();
        
        try
        {
            diskFile = new PrintWriter(fileName);
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        writeHelper(root,diskFile,"");
        diskFile.close();
    }
    public void writeHelper(Node node, PrintWriter p, String path)
    {
        if(node == null)
        {
            return;
        }
        else if(node.left == null && node.right == null)
        {
            //leaf node
            map.put(node.myChar,path);
            p.println(node.myChar);
            p.println(path);
        }
        else
        {
            writeHelper(node.left,p,path + "0");
            writeHelper(node.right,p,path + "1");
        }
    }
    public void encode(BitOutputStream out, String fileName)
    {
        //use void write(int b)
        Scanner scan = null;
        try
        {
            scan = new Scanner(new File(fileName));
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        
        while(scan.hasNextLine())
        {
            String str = scan.nextLine();
            for(int element : str.toCharArray())
            {
                String path = map.get(element);
                for(int i = 0; i < path.length(); i++)
                {
                    out.writeBit(Integer.parseInt(path.substring(i,i+1)));
                }
            }
            //adds space
            if(scan.hasNext())
            {
                String space = map.get((int)'\n');
                for(int i = 0; i < space.length(); i++)
                {
                    out.writeBit(Integer.parseInt(space.substring(i,i+1)));
                }
            }
        }
        //Add EOF Character
        String path = map.get(256);
        for(int i = 0; i < path.length(); i++)
        {
            out.writeBit(Integer.parseInt(path.substring(i,i+1)));
        }
        
        out.close();
    }
    public void decode(BitInputStream in, String outFile)
    {
        PrintWriter p = null;
        try
        {
            p = new PrintWriter(outFile);
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        Node current = root;
        while(true)
        {
            int num = in.readBit();
            //left or right
            if(num == 0)
            {
                current = current.left;
            }
            else
            {
                current = current.right;
            }
            
            //leaf node
            if(current.left == null && current.right == null)
            {
                if(current.myChar.equals(256))
                {
                    break;//ends loop because of EOF Character
                }
                else
                {
                    p.print(Character.toString((char) (int)current.myChar));
                }
                current = root;
            }
        }
        in.close();
        p.close();
    }
    /*public Node getRoot()
    {
        return root;
    }*/
    public static void main(String[] args)
    {
        //TESTING
        /*Scanner scan = null;
        try
        {
            scan = new Scanner(new File("happy hip hop.txt"));
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        int[] freq = new int[256];
        while(scan.hasNextLine())
        {
            String str = scan.nextLine();
            for(int element : str.toCharArray())
            {
                freq[element]++;
            }
        }
        HuffmanTree t = new HuffmanTree(freq);
        TreePrinter.printTree(t.getRoot());*/
        String fileName = "War and Peace";
        HuffmanCompressor.compress(fileName + ".txt");
        HuffmanCompressor.expand(fileName + ".code",fileName + ".short");
    }
}
class Node implements Comparable<Node>
{
    int weight;
    Integer myChar;
    Node left;
    Node right;
    public Node(int weight, Integer myChar)
    {
        this.weight = weight;
        this.myChar = myChar;
    }
    public int compareTo(Node other)
    {
        return this.weight - other.weight;
    }
    public String toString()
    {
        return weight + " ";
    }
}