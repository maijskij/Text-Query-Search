package search.android.aboutall.eu.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Searcher {

    // letter -> number mapping
    private static Map<String, String>  sDigits;
    static{
        sDigits = new HashMap<>();
        sDigits.put("a","2");
        sDigits.put("b","2");
        sDigits.put("c","2");
        sDigits.put("d","3");
        sDigits.put("e","3");
        sDigits.put("f","3");
        sDigits.put("g","4");
        sDigits.put("h","4");
        sDigits.put("i","4");
        sDigits.put("l","5");
        sDigits.put("k","5");
        sDigits.put("m","5");
        sDigits.put("m","6");
        sDigits.put("n","6");
        sDigits.put("o","6");
        sDigits.put("p","7");
        sDigits.put("q","7");
        sDigits.put("r","7");
        sDigits.put("s","7");
        sDigits.put("t","8");
        sDigits.put("u","8");
        sDigits.put("v","8");
        sDigits.put("w","9");
        sDigits.put("x","9");
        sDigits.put("y","9");
        sDigits.put("z","9");
    }

    private Map<String,Set<String>> mIndexedData;

    void loadDictionary(Iterable<String> words){

        if (words == null)
            return;

        mIndexedData = new HashMap<>();
        for (String word: words){
            //get all "keys" (e.g. "12", "123", "1234") which should trigger word to appear
            List<String> keys = getWordCodes(word.toLowerCase());

            for (String key: keys){
                // crete appropriate key in dictionary
                if (!mIndexedData.containsKey(key)){
                    mIndexedData.put(key, new HashSet<String>());
                }
                // add it to indexed dictionary
                mIndexedData.get(key).add(word);
            }
        }
    }

    List<String> lookup(String digits) {

        if (mIndexedData != null && mIndexedData.containsKey(digits)){
            List<String> result = new ArrayList<>(mIndexedData.get(digits));
            //sort reusults
            Collections.sort(result);
            return result;
        }else{
            return null;
        }
    }

    private String getWordCode(String word) {
        StringBuilder code = new StringBuilder();
        // word's code is all it's letter's appropriate digit symbols
        for (int i = 0; i < word.length(); i++){
            String symbol = word.substring(i, i + 1);
            String num = sDigits.get(symbol);
            if (num != null){
                code.append(num);
            }
        }

        return code.toString();
    }

    private List<String> getWordCodes(String word){
        // get word's code
        String key = getWordCode(word);
        List<String> result = new ArrayList<>(key.length());
        // word should appear, aslo when substring prefix code of this word is found
        for (int i = 0; i < key.length(); i++){
            result.add(key.substring(0, i +1 ));
        }

        return result;
    }

}
