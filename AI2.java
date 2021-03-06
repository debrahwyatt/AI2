/*
 * This AI program deconstructs a user input, builds statistics, and generates
 * a new sentence on its own.
 */
package ai2;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import toolbox.*;

/**
 * @author Debrah Wyatt
 */
public class AI2 {

    //Variables created by UserInput.
    public static String userInput;
    private static int avgWordSize;
    private static int maxWordSize;

    //This array stores the frequency of characters used.
    private static final int[] letterPercent = new int[93];

    public static void main(String[] args) throws IOException {
        System.out.println(AiString());
    }

    //This method deconstructs the user's String.
    public static void UserInput() {
        int currentWord = 0;
        int letterCount = 0;
        int wordCount = 0;

        // User input String.
        userInput = "The best day I ever had at school was the day I met you.";

        //Converts userInput to lower case for ease of use.
        userInput = userInput.toLowerCase();

        try {

            //Initiallizes letterPercent by adding 1 to each element.
            for (int i = 0; i < 93; i++) {
                AI2.letterPercent[i]++;
            }

            //Checks every character for variety.
            for (int i = 0; i < userInput.length(); i++) {
                char currentChar = userInput.charAt(i);

                // Counts characters from a to z.
                if (currentChar >= 33 && currentChar <= 126) {
                    letterCount++;
                    currentWord++;
                    AI2.letterPercent[userInput.charAt(i) - 33]++;
                }

                // Counts spaces for words and finds the max word size.
                if (currentChar == ' ' || (i + 1) == userInput.length()) {
                    wordCount++;
                    if (currentWord > maxWordSize) {
                        maxWordSize = currentWord;
                    }
                    currentWord = 0;
                }
            }

            //Calculates the average word size.
            avgWordSize = letterCount / wordCount;

            //Prints out the useful information
            // System.out.println("userInput = " + userInput);
            // System.out.println("wordCount = " + wordCount);
            // System.out.println("AvgWordSize = " + avgWordSize);
            // System.out.println("maxWordSize = " + maxWordSize);
            // System.out.println("AiWordLength: " + AiWordLength());
            // System.out.println("AiCharStats: " + AiCharStats());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //This method will return the Ai's word length
    public static int AiWordLength() {
        int topRank;
        int totalRank = 0;
        topRank = maxWordSize + 1;

        //Min to max word range
        int[] rank = new int[maxWordSize];
        int[] rankPercent = new int[maxWordSize];

        //Associates the word size with its rank and finds the sum of all ranks.
        for (int i = 0; i < rank.length; i++) {
            rank[i] = Math.abs(Math.abs(i - avgWordSize) - topRank);
            totalRank += rank[i];
        }

        //Converts rank into % likelyhood
        for (int i = 0; i < rankPercent.length; i++) {
            rankPercent[i] = rank[i] * 1000 / totalRank;
        }

        //Stacks the percentages to add up to 1000%
        int[] percentStack = new int[maxWordSize];
        percentStack[0] = rankPercent[0];
        for (int i = 1; i < percentStack.length; i++) {
            percentStack[i] = percentStack[i - 1] + rankPercent[i];
        }

        //Randomizer to pick the word size based on probability
        Random random = new Random();
        int pickWordSize = random.nextInt(1000);
        for (int i = 0; i < maxWordSize; i++) {
            if (pickWordSize <= percentStack[i]) {
                return (i + 1);
            }
        }
        return avgWordSize;
    }

    //Returns a character based on probability
    public static char AiCharStats() {
        int rank = 0;
        Random random = new Random();

        //rank = the sum of every element in the count array.
        for (int i = 0; i < letterPercent.length; i++) {
            rank = rank + letterPercent[i];
        }

        //Array creates a percent rank for each letter
        int[] rankPercent = new int[93];
        for (int i = 0; i < letterPercent.length; i++) {
            rankPercent[i] = letterPercent[i] * 10000 / rank;
        }

        //Stacks the percentages to add up to 10000%
        int[] percentStack = new int[93];
        percentStack[0] = rankPercent[0];
        for (int i = 1; i < letterPercent.length; i++) {
            percentStack[i] = percentStack[i - 1] + rankPercent[i];
        }

        //Randomizer to pick the letter based on probability
        int pickChar = random.nextInt(10000);
        for (int i = 0; i < letterPercent.length; i++) {
            if (pickChar <= percentStack[i]) {
                char probableChar = (char) (i + 33); //Converts int to char
                return (probableChar);
            }
        }
        return (char) (random.nextInt(93) + 33);
    }

    //This method creates a string of words.
    public static String AiString() throws IOException {
        UserInput();
        String aiString = "";
        int aiStringLength = 0;

        //Turns a word list into an array.
        File file = new File("C:\\Users\\devli\\OneDrive\\Documents\\NetBeansProjects\\AI2\\src\\ai2\\3000-Common-Words.txt");
        Initialize wL = new Initialize();
        String[] wordList = wL.Initialization(file);

        //Finds the longest word in the wordlist.
        int longWord = 0;
        for (int i = 0; i < wordList.length; i++) {
            if (wordList[i].length() > longWord) {
                longWord = wordList[i].length();
            }
        }

        //Reduces maxWordSize to the longest word found.
        if (maxWordSize > longWord) {
            maxWordSize = longWord;
        }

        //Creates an array of compatible word lengths.
        boolean[] compatibleWordLengths = new boolean[maxWordSize];
        for (int i = 0; i < wordList.length; i++) {
            for (int j = 0; j < compatibleWordLengths.length; j++) {
                if (wordList[i].length() == j + 1) {
                    compatibleWordLengths[j] = true;
                }
            }
        }

        //Creates the ai string
        while (aiStringLength < userInput.length()) {
            String aiWord;

            //Omits word lengths that are incompatible.
            int thisWordLength = 0;
            boolean lengthOK = false;
            do {
                thisWordLength = AiWordLength();
                for (int i = 0; i < compatibleWordLengths.length; i++) {
                    if (i + 1 == thisWordLength && compatibleWordLengths[i] == true) {
                        lengthOK = true;
                    }
                }
            } while (lengthOK == false);

            //Creates the next word.
            char[] currentWord = new char[thisWordLength];
            for (int j = 0; j < currentWord.length; j++) {
                currentWord[j] = AiCharStats();
                boolean compatible = false;
                for (int i = 0; i < wordList.length; i++) {
                    if (currentWord.length == wordList[i].length()) {
                        for (int k = 0; k <= j; k++) {
                            if (wordList[i].charAt(k) != currentWord[k]) {
                                compatible = false;
                                break;
                            } else if (wordList[i].charAt(k) == currentWord[k]) {
                                compatible = true;
                            }
                        }
                    }
                    if (compatible == true) {
                        break;
                    }
                    if (i == wordList.length - 1) {
                        j--;
                        break;
                    }
                }
            }
            //Converts currentWord to a String
            aiWord = new String(currentWord);

            //Capitalizes 'i'
            if (aiWord.equalsIgnoreCase("i")) {
                aiWord = aiWord.toUpperCase();
            }

            //Combines the Strings
            aiString = aiString + aiWord + " ";

            //Capitalizes the first letter of the String
            aiString = aiString.substring(0, 1).toUpperCase() + aiString.substring(1);

            //Tracks the string length
            aiStringLength = aiStringLength + thisWordLength;
        }
        return aiString;
    }
}
