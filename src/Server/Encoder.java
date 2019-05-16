package Server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class Encoder
{

    public class Pair
    {
        String type;
        int value;

        Pair(String type, int value)
        {
            this.type = type;
            this.value = value;
        }
    }

    private HashMap<String, Integer> encoded;
    private HashMap<Integer, Integer> encodePair;
    private HashMap<String, Integer> value;
    private ArrayList<ArrayList<Integer>> transactions;

    private int lastIdx;
    private String lastIP;
    private int lastTransactionIdx;

    Encoder()
    {
        encoded = new HashMap<>();
        encodePair = new HashMap<>();
        value = new HashMap<>();
        transactions = new ArrayList<>();

        value.put("Course viewed", 1);
        value.put("Course module viewed", 1);
        value.put("User enrolled in course", 2);
        value.put("User unenrolled from course", 2);
        value.put("Course module created", 3);
        value.put("Log report viewed", 1);
        value.put("Role assigned", 2);
        value.put("Role unassigned", 3);
        value.put("Grade user report viewed", 3);
        value.put("Recent activity viewed", 1);
        value.put("User list viewed", 1);
        value.put("User profile viewed", 1);
        value.put("Grade overview report viewed", 2);
        value.put("User report viewed", 1);
        value.put("Course module updated", 2);
        value.put("Course section updated", 2);
        value.put("Calendar event created", 3);
        value.put("Course searched", 4);
        value.put("Discussion viewed", 2);
        value.put("Course user report viewed", 2);
        value.put("Enrolment instance created", 1);
        value.put("Item created", 1);
        value.put("Calendar event updated", 1);
        value.put("Grader report viewed", 2);
        value.put("Grouping deleted", 2);
        value.put("Some content has been posted.", 2);
        value.put("Discussion created", 2);
        value.put("Course module deleted", 2);
        value.put("Activity report viewed", 2);
        value.put("Live log report viewed", 3);
        value.put("Course module instance list viewed", 2);

        lastIdx = 1;
        lastIP = null;
        lastTransactionIdx = -1;
    }

    void encode(String toEncode, String type, String ip)
    {
        int idx;
        boolean found = false;

        if (encoded.containsKey(toEncode))
        {
            idx = encoded.get(toEncode);
        }
        else
        {
            idx = lastIdx;
            encoded.put(toEncode, lastIdx);
            found = true;
        }

        if (value.containsKey(type))
        {
            encodePair.put(lastIdx, value.get(type));
        }
        else
        {
            System.out.println(type);
            encodePair.put(lastIdx, 0);
        }

        if (!ip.equals(lastIP))
        {
            lastTransactionIdx++;
            transactions.add(new ArrayList<>());
            lastIP = ip;
        }

        if (!transactions.get(lastTransactionIdx).contains(idx))
        {
            transactions.get(lastTransactionIdx).add(idx);
        }

        if (found)
        {
            lastIdx++;
        }
    }

    public int getValue(int itemID)
    {
        if (encodePair.containsKey(itemID))
        {
            return encodePair.get(itemID);
        }
        return -1;
    }

    String getDecoded(String str)
    {
        String[] spl = str.split(" ");
        StringBuilder ans = new StringBuilder("Actions of type: ");
        for (String s : spl)
        {
            int idx = Integer.parseInt(s);
            for (Map.Entry<String, Integer> e : encoded.entrySet())
            {
                if (e.getValue().equals(idx))
                {
                    String[] getType = e.getKey().split(":");
                    ans.append(getType[0]);
                    ans.append("[");
                    ans.append(getType[1]);
                    ans.append("]");
                    ans.append(", ");
                }
            }

        }

        ans.deleteCharAt(ans.length() - 1);
        ans.deleteCharAt(ans.length() - 1);
        return ans.toString();
    }

    void writeToFile(File tempFile) throws IOException
    {
        PrintWriter output = new PrintWriter(new FileWriter(tempFile));
        for (ArrayList<Integer> arr : transactions)
        {
            Collections.sort(arr);

            StringBuilder first = new StringBuilder();
            StringBuilder last = new StringBuilder();
            int total = 0, curr;
            for (int i : arr)
            {
                if (first.length() != 0)
                {
                    first.append(" ");
                }
                first.append(i);

                curr = getValue(i);
                if (last.length() != 0)
                {
                    last.append(" ");
                }
                last.append(curr);

                total += curr;
            }

            String toWrite = first.toString() + ":" + total + ":" + last.toString();
            output.println(toWrite);
        }

        output.flush();
        output.close();
    }
}
