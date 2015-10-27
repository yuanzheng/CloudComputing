import java.io.FileReader;
import java.io.BufferedReader;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.StringTokenizer;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MP1 {
    Random generator;
    String userName;
    String inputFileName;
    String delimiters = " \t,;.?!-:@[](){}_*+/";
    String[] stopWordsArray = {"i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours",
            "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its",
            "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that",
            "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having",
            "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while",
            "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before",
            "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again",
            "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each",
            "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than",
            "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"};

    HashMap<String, Integer> trace = new HashMap<String, Integer>();

    void initialRandomGenerator(String seed) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        messageDigest.update(seed.toLowerCase().trim().getBytes());
        byte[] seedMD5 = messageDigest.digest();

        long longSeed = 0;
        for (int i = 0; i < seedMD5.length; i++) {
            longSeed += ((long) seedMD5[i] & 0xffL) << (8 * i);
        }

        this.generator = new Random(longSeed);
    }

    Integer[] getIndexes() throws NoSuchAlgorithmException {
        Integer n = 10000;
        Integer number_of_lines = 50000;
        Integer[] ret = new Integer[n];
        this.initialRandomGenerator(this.userName);
        for (int i = 0; i < n; i++) {
            ret[i] = generator.nextInt(number_of_lines);
        }
        return ret;
    }

    public MP1(String userName, String inputFileName) {
        this.userName = userName;
        this.inputFileName = inputFileName;
    }

    private ArrayList<String> preprocess(String msg, List lList) {
        StringTokenizer st = new StringTokenizer(msg, this.delimiters);
        ArrayList<String> list = new ArrayList<String>();

        while(st.hasMoreTokens()) {
            String token = st.nextToken().toLowerCase().trim();
            if (!lList.contains(token))
                list.add(token);
        }
        return list;
    }



    private void findTop20Words(String message, List lList) {
        
        ArrayList<String> listOfWords = preprocess(message, lList);
        Iterator<String> it = listOfWords.iterator();

        int index = 0;
        while (it.hasNext()) {
            String theNext = it.next();
            if (trace.containsKey(theNext))
            {
                int value = trace.get(theNext) + 1;
                trace.put(theNext, value);
            } else {
                trace.put(theNext, 1);
            }   
        }
    }

    private ArrayList<String> getEntireFile() throws FileNotFoundException, IOException {
        String message;
        
        ArrayList<String> listWords = new ArrayList<String>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.inputFileName));
            int index = 0;
            while ((message = reader.readLine()) != null)
            {
                listWords.add(message);   
            }
            reader.close();
        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage());
            throw ex;
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            throw ex;
        }

        return listWords;
    }

    public String[] process() throws Exception {
        String[] ret = new String[20];
       
        ArrayList<String> listOfRows = getEntireFile();
        Integer[] num = getIndexes();
        List lList = Arrays.asList(stopWordsArray);

        for (int i=0; i<num.length; i++) {
            findTop20Words(listOfRows.get(num[i]), lList);
        }

        for(String key : trace.keySet()) {
            Integer value = trace.get(key);
        }
 
        Map<String, Integer> sortedMap = sortByValues(trace);

        Set set = sortedMap.entrySet();
 
        // Get an iterator
        Iterator i = set.iterator();
     
        int counter = 0;
        // Display elements
        while(i.hasNext()) {
            if(counter < 20) {
                Map.Entry me = (Map.Entry)i.next();
                ret[counter++] = me.getKey().toString();
            }
            else
                break;
        }
       
        return ret;
    }

    public static <K, V extends Comparable<V>> Map<K, V> sortByValues(final Map<K, V> map) {
        Comparator<K> valueComparator = new Comparator<K>() {
          public int compare(K k1, K k2) {
            int compare = map.get(k1).compareTo(map.get(k2));
            if (compare == 0) {
                int keyCompare = k1.toString().compareTo(k2.toString());
                if(keyCompare == 0) 
                    return 1;
                else
                    return keyCompare;
            }
            else 
              return -1*compare;
          }
        };

        Map<K, V> sortedByValues = new TreeMap<K, V>(valueComparator);
        sortedByValues.putAll(map);
        return sortedByValues;
    }   

    public static void main(String[] args) throws Exception {
        if (args.length < 1){
            System.out.println("MP1 <User ID>");
        }
        else {
            String userName = args[0];
            String inputFileName = "./input.txt";
            MP1 mp = new MP1(userName, inputFileName);

            try {
                String[] topItems = mp.process();
                for (String item: topItems){
                    System.out.println(item);
                }

            } catch (Exception e) {
                System.err.println("Stop running" + e);
            }
        }
    }
}
